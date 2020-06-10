package ink.ptms.cronus.database.impl

import com.mongodb.BasicDBObject
import com.mongodb.ConnectionString
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.*
import ink.ptms.cronus.Cronus
import ink.ptms.cronus.database.Database
import ink.ptms.cronus.database.data.DataPlayer
import io.izzel.taboolib.internal.gson.*
import io.izzel.taboolib.internal.gson.annotations.JsonAdapter
import io.izzel.taboolib.internal.gson.internal.ConstructorConstructor
import io.izzel.taboolib.module.db.local.SecuredFile
import io.izzel.taboolib.util.Files
import io.izzel.taboolib.util.serialize.TSerializer
import org.bson.BsonArray
import org.bson.BsonDocument
import org.bson.BsonDocumentWriter
import org.bson.Document
import org.bson.codecs.BsonTypeClassMap
import org.bson.conversions.Bson
import org.bukkit.Bukkit
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import java.io.ObjectOutput
import java.io.ObjectOutputStream
import java.io.OutputStreamWriter
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.collections.ArrayList

/**
 * @Author sky
 * @Since 2020-03-12 19:31
 */
class DatabaseMongoDB : Database() {

    var uniqueId = false
    var mongoClient: MongoClient? = null
    var mongoDatabase: MongoDatabase? = null
    var mongoCollectionData: MongoCollection<Document>? = null
    val serializer = GsonBuilder().excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapter(YamlConfiguration::class.java, JsonSerializer<YamlConfiguration> { a, _, _ -> JsonPrimitive(a.saveToString()) })
            .registerTypeAdapter(YamlConfiguration::class.java, JsonDeserializer<YamlConfiguration> { a, _, _ -> SecuredFile.loadConfiguration(a.asString) }).create()

    override fun init() {
        uniqueId = Cronus.getConf().getBoolean("Database.uniqueId")
        mongoClient = MongoClients.create(ConnectionString(Cronus.getConf().getString("Database.mongodb.client")!!))
        mongoDatabase = mongoClient!!.getDatabase(Cronus.getConf().getString("Database.mongodb.database")!!)
        mongoCollectionData = mongoDatabase!!.getCollection(Cronus.getConf().getString("Database.mongodb.collection")!!)
        if (mongoCollectionData!!.listIndexes().none { it.get("key", Document::class.java).containsKey("player") }) {
            mongoCollectionData!!.createIndex(Indexes.ascending("player"))
        }
    }

    override fun cancel() {
        mongoClient!!.close()
    }

    override fun upload0(player: Player, dataPlayer: DataPlayer) {
        if (mongoCollectionData!!.countDocuments(Filters.eq("player", toName(player))) == 0L) {
            mongoCollectionData!!.insertOne(Document.parse(serializer.toJson(dataPlayer)).append("player", toName(player)))
        } else {
            try {
                val current = jsonToMap(serializer.toJsonTree(dataPlayer).asJsonObject)
                val difference = difference(current, dataPlayer.latestUpdate)

                // debug
                if (Cronus.getConf().getBoolean("Database.debug") && player.isOp) {
                    println("[DEBUG] upload ${player.name}'s quest data")
                    println("[DEBUG] origin ${serializer.toJsonTree(dataPlayer).asJsonObject}")
                    println("[DEBUG] difference [")
                    difference.forEach {
                        println("[DEBUG] - ${it.first} : ${it.second}")
                    }
                    println("[DEBUG] ]")
                }

                if (difference.isEmpty()) {
                    return
                }
                mongoCollectionData!!.updateOne(Filters.eq("player", toName(player)), Updates.combine(toBson(difference)))
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
        dataPlayer.latestUpdate.clear()
        dataPlayer.latestUpdate.putAll(jsonToMap(serializer.toJsonTree(dataPlayer).asJsonObject))
    }

    override fun download0(player: Player): DataPlayer? {
        val data: DataPlayer
        val find = mongoCollectionData!!.find(Filters.eq("player", toName(player))).firstOrNull()
        if (find != null) {
            data = serializer.fromJson(find.toJson(), DataPlayer::class.java)
            data.player = player
        } else {
            data = DataPlayer(player)
        }
        data.latestUpdate.putAll(jsonToMap(serializer.toJsonTree(data).asJsonObject))
        return data
    }

    override fun setGV0(key: String, value: String?) {
        if (mongoCollectionData!!.countDocuments(Filters.eq("player", "\$variables")) == 0L) {
            mongoCollectionData!!.insertOne(Document("player", "\$variables").append(key, value))
        } else if (value != null) {
            mongoCollectionData!!.updateOne(Filters.eq("player", "\$variables"), Updates.set(key, value))
        } else {
            mongoCollectionData!!.updateOne(Filters.eq("player", "\$variables"), Updates.unset(key))
        }
    }

    override fun getGV0(key: String): String? {
        return mongoCollectionData!!.find(Filters.eq("player", "\$variables")).firstOrNull()?.getString(key)
    }

    override fun getGV0(): Map<String, String>? {
        return mongoCollectionData!!.find(Filters.eq("player", "\$variables")).firstOrNull()?.toMap()?.mapValues { it.toString() } ?: emptyMap()
    }

    @Suppress("UNCHECKED_CAST")
    fun toBson(difference: List<Pair<String, Any?>>): List<Bson> {
        return difference.map {
            if (it.second == null) {
                Updates.unset(it.first)
            } else if (it.second is Map<*, *>) {
                Updates.set(it.first, Document.parse(serializer.toJson(it.second)))
            } else {
                Updates.set(it.first, it.second)
            }
        }.toList()
    }

    @Suppress("UNCHECKED_CAST")
    fun difference(current: Map<*, *>, origin: Map<*, *>): List<Pair<String, Any?>> {
        val difference = ArrayList<Pair<String, Any?>>()
        // chance & add
        current.forEach { (k, v) ->
            if (v is Map<*, *> && origin[k] is Map<*, *>) {
                difference.addAll(difference(v as Map<String, Any>, origin[k] as Map<String, Any>).map { "$k.${it.first}" to it.second })
            } else if (v != origin[k]) {
                difference.add(k.toString() to v)
            }
        }
        // delete
        origin.forEach { (k, v) ->
            if (v is Map<*, *> && current[k] is Map<*, *>) {
                difference.addAll(difference(v as Map<String, Any>, origin[k] as Map<String, Any>).map { "$k.${it.first}" to it.second })
            } else if (!current.containsKey(k)) {
                difference.add(k.toString() to null)
            }
        }
        return difference
    }

    fun jsonToMap(json: JsonObject): Map<*, *> {
        return Gson().fromJson(json, Map::class.java)
    }

    fun toName(player: Player): String {
        return if (uniqueId) player.uniqueId.toString() else player.name
    }
}

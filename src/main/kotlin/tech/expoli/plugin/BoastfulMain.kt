package tech.expoli.plugin

import net.mamoe.mirai.console.command.ContactCommandSender
import net.mamoe.mirai.console.command.registerCommand
import net.mamoe.mirai.console.plugins.PluginBase
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.event.subscribeFriendMessages
import net.mamoe.mirai.event.subscribeGroupMessages


object BoastfulMain : PluginBase() {

    private var boastful: Boastful? = null
    private const val lostPath = "lots.yml"
    private const val configPath = "config.yml"
    private val config = loadConfig(configPath)

    private val groupList by lazy {
        config.setIfAbsent("groups", mutableListOf<Long>())
        config.getLongList("groups").toMutableSet()
    }

    override fun onLoad() {
        super.onLoad()
        logger.info("onLoad")
        boastful = Boastful(lostPath)
        logger.info("Boastful init success!")

    }

    override fun onEnable() {
        logger.info("Boastful enabled success!")
        super.onEnable()
        registerCommands()

        subscribeGroupMessages {
            (case("夸夸我", ignoreCase = true, trim = true) or
                    case("撩撩我", ignoreCase = true, trim = true) or
                    contains("撩一撩")){
                if (groupList.contains(this.group.id)) {
                    this.quoteReply(
                        boastful!!.sign(sender.id)
                    )
                }
            }
        }

        subscribeFriendMessages {
            (case("撩撩我", ignoreCase = true, trim = true) or contains("撩一撩")){
                this.reply(boastful!!.sign(sender.id))
            }
        }
    }

    override fun onDisable() {
        super.onDisable()
        logger.info("Boastful Disabling")
        config["groups"] = groupList.toList()
        config.save()
        logger.info("Boastful Saving config")
        boastful = null
    }

    // 注册命令
    private fun registerCommands() {
        registerCommand {
            name = "Boastful"
            alias = listOf("KKW")
            description = "DrawLots插件命令管理"
            usage = "[/KKW enable] 打开本群的夸夸其谈模式(仅限群里控制)\n" +
                    "[/KKW disable] 关闭本群的夸夸其谈模式(仅限群里控制)\n" +
                    "[/KKW enable 群号] 打开指定群的夸夸其谈模式\n" +
                    "[/KKW disable 群号] 关闭指定群的夸夸其谈模式"
            onCommand {
                if (it.isEmpty()) {
                    return@onCommand false
                }
                when (it[0].toLowerCase()) {
                    "enable" -> {
                        val groupID: Long = if (it.size == 1) {
                            if (this is ContactCommandSender && this.contact is Group) { //判断是否在群里发送的命令
                                this.contact.id
                            } else {
                                return@onCommand false
                            }
                        } else {
                            it[1].toLong()
                        }
                        groupList.add(groupID)
                        this.sendMessage("群${groupID}:已开启夸夸其谈模式")
                        return@onCommand true
                    }
                    "disable" -> {
                        val groupID = if (it.size == 1) {
                            if (this is ContactCommandSender && this.contact is Group) { //判断是否在群里发送的命令
                                this.contact.id
                            } else {
                                return@onCommand false
                            }
                        } else {
                            it[1].toLong()
                        }
                        groupList.remove(groupID)
                        this.sendMessage("群${groupID}:已关闭夸夸其谈模式")
                        return@onCommand true
                    }
                    else -> {
                        return@onCommand false
                    }
                }
            }
        }
    }
}
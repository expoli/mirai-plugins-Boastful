package tech.expoli.plugin

import net.mamoe.mirai.console.plugins.PluginBase
import net.mamoe.mirai.event.events.MessageRecallEvent
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.utils.info

object BoastfulMain : PluginBase() {
    override fun onLoad() {
        super.onLoad()
    }

    override fun onEnable() {
        super.onEnable()

        logger.info("Plugin loaded!")

        subscribeMessages {
            "greeting" reply { "Hello ${sender.nick}" }
        }

        subscribeAlways<MessageRecallEvent> { event ->
            logger.info { "${event.authorId} 的消息被撤回了" }
        }
    }
}
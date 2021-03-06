package tech.expoli.plugin

import net.mamoe.mirai.console.plugins.ConfigSection
import java.util.*

class Boastful(lotsPath: String) {

    private var lots: List<ConfigSection>?
    private var lotMap = mutableMapOf<Long, Int>()
    private var day: Int

    /**
     * 初始化，加载数据
     */
    init {
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        try {
            lots = BoastfulMain.getResourcesConfig(lotsPath).getConfigSectionList("Lots")
            BoastfulMain.logger.info("本地夸夸其谈数据加载成功,数据大小为：" + lots!!.size)
        } catch (e: Exception) {
            e.printStackTrace()
            BoastfulMain.logger.info("无法加载本地夸夸其谈数据")
            lots = null
        }
    }

    /**
     *  抽取话语
     */
    fun sign(id: Long): String {
        return if (lots != null) {
            val index = lots!!.indices.random()
            lotMap[id] = index
            val lot = lots!![index]
            "第" + lot.getString("uid") + "条\n" +
                    lot.getString("sign") + "^_^"
            // }
        } else {
            "Lots init failed!"
        }
    }
}


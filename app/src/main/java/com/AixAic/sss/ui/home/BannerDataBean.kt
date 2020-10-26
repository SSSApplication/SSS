package com.AixAic.sss.ui.home

class BannerDataBean {
    var imageRes: Int? = null
    var imageUrl: String? = null
    var title: String?
    var viewType: Int

    constructor(imegeRes: Int?, title: String?, viewType: Int) {
        this.imageRes = imageRes
        this.title = title
        this.viewType = viewType
    }
    constructor(imageUrl: String?, title: String?, viewType: Int) {
        this.imageUrl = imageUrl
        this.title = title
        this.viewType = viewType
    }

    companion object {
        val testData: List<BannerDataBean> get() {
            val list: MutableList<BannerDataBean> = ArrayList()
            list.add(
                BannerDataBean("https://img.zcool.cn/community/016a2256fb63006ac7257948f83349.jpg", null, 1)
            )
            list.add(
                BannerDataBean("https://img.zcool.cn/community/01233056fb62fe32f875a9447400e1.jpg", null, 1)
            )
            list.add(
                BannerDataBean("https://img.zcool.cn/community/01270156fb62fd6ac72579485aa893.jpg", null, 1)
            )
            return list
        }

    }

}
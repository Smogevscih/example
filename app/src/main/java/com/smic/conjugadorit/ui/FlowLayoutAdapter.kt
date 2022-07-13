package com.smic.conjugadorit.ui

import android.view.View

/**
 * @author Smogevscih Yuri
25.03.2022
 **/
abstract class FlowLayoutAdapter<T> {
    //保存所有的item数据
    lateinit var itemDatas: List<T>

    constructor(itemDatas: List<T>) {
        this.itemDatas = itemDatas
    }

    /**
     * 获取item view 数量
     */
    fun getCount(): Int {
        return itemDatas.size
    }

    /**
     * 获取data
     */
    fun getData(position: Int): T {
        return itemDatas[position]
    }



    abstract fun getView(flowLayout: FlowLayout, position: Int, data: Any): View
}
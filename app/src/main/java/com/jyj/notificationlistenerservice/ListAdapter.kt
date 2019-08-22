package com.jyj.notificationlistenerservice

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.jyj.notificationlistenerservice.bean.DataBean


/**
author :shengsheng
date :2019/8/5
i despised my soul when i committed a wrong,and comforted myself that others also commit wrong.
 */
class ListAdapter : BaseQuickAdapter<DataBean, BaseViewHolder> {
    constructor() : super(R.layout.item)

    override fun convert(helper: BaseViewHolder?, item: DataBean?) {
        if (item != null) {
            helper?.setText(R.id.tvTime, item.time)
            helper?.setText(R.id.tvName, item.name)
            helper?.setText(R.id.tvMoney, item.money)
        }
    }
}
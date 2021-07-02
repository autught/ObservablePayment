package com.autught.livepay.alipay

import com.alipay.sdk.app.PayTask
import com.autught.livepay.PayResult
import com.autught.livepay.PayStatus

/**
 * @description:
 * @author:  79120
 * @date :   2021/7/2 16:37
 */
class Alipay {
    private fun createPaymentStatus(payTask: PayTask, orderInfo: String): Boolean {
        val result = payTask.payV2(orderInfo, true)
        val payResult = PayResult(result)
        return payResult.resultStatus == "9000"
    }

    /*
     返回码	含义
     9000	订单支付成功
     8000	正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
     4000	订单支付失败
     5000	重复请求
     6001	用户中途取消
     6002	网络连接出错
     6004	支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
     其它	其它支付错误
    */
    private fun resolvePayResult(payResult: PayResult): PayStatus {
        return when (payResult.resultStatus) {
            "9000" -> PayStatus(true)
            "8000", "6004" -> PayStatus(false, "正在处理中")
            "4000" -> PayStatus(false, "订单支付失败")
            "5000" -> PayStatus(false, "重复请求")
            "6001" -> PayStatus(false, "已取消支付")
            "6002" -> PayStatus(false, "网络连接出错")
            else -> PayStatus(false, "支付失败")
        }
    }
}
import request from '@/utils/request'

const api_name = '/admin/statistics'

export default {

    //预约统计数据
    getCountMap(searchObj) {
        return request({
            url: `${api_name}/getCountMap`,
            method: 'get',
            params: searchObj
        })
    },
    //就诊人列表
    findList() {
        return request({
          url: `${api_name}/getPatients`,
          method: 'get'
        })
    },
    //订单分页列表
    getPageList(page, limit, searchObj) {
        return request({
          url: `${api_name}/getOrders/${page}/${limit}`,
          method: 'get',
          params: searchObj
        })
    },
    //订单详情
    getOrderInfo(orderId) {
        return request({
          url: `${api_name}/getOrderInfo/${orderId}`,
          method: 'get'
        })
    }
}

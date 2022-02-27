import request from '@/utils/request'

const api_name = `/api/order/orderInfo`

export default {
  getPageList(page, limit, searchObj) {
    return request({
      url: `${api_name}/auth/${page}/${limit}`,
      method: 'get',
      params: searchObj
    })
  },

  getStatusList() {
    return request({
      url: `${api_name}/auth/getStatusList`,
      method: 'get'
    })
  },

  getOrderInfo(orderId) {
    return request({
      url: `${api_name}/auth/getOrderInfo/${orderId}`,
      method: 'get'
    })
  }

}

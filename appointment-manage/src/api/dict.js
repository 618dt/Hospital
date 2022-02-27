import request from '@/utils/request'

export default {
    dictList(id) {//数据字典列表
      return request ({
        url: `/admin/cmn/dict/findChildData/${id}`,
        method: 'get'
      })
    }
  }
  
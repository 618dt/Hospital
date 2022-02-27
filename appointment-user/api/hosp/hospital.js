import request from '@/utils/request'

const api_name = `/api/hosp/hospital`

export default {
  //获取医院列表
  getPageList(page, limit, searchObj) {
    return request({
      url: `${api_name}/findHospList/${page}/${limit}`,
      method: 'get',
      params: searchObj
    })
  },
  //获取医院名称
  getByHosname(hosname) {
    return request({
      url: `${api_name}/findByHosName/${hosname}`,
      method: 'get'
    })
  },
  //显示医院详情
  show(hoscode) {
    return request({
      url: `${api_name}/findHospDetail/${hoscode}`,
      method: 'get'
    })
  },
  //根据医院编号获取科室
  findDepartment(hoscode) {
    return request({
      url: `${api_name}/department/${hoscode}`,
      method: 'get'
    })
  },
  //获取预约规则
  getBookingScheduleRule(page, limit, hoscode, depcode) {
    return request({
      url: `${api_name}/auth/getBookingScheduleRule/${page}/${limit}/${hoscode}/${depcode}`,
      method: 'get'
    })
  },
  //获取排班详细数据
  findScheduleList(hoscode, depcode, workDate) {
    return request({
      url: `${api_name}/auth/findScheduleList/${hoscode}/${depcode}/${workDate}`,
      method: 'get'
    })
  },
  //根据排班id获取排班数据
  getSchedule(id) {
    return request({
      url: `${api_name}/getSchedule/${id}`,
      method: 'get'
    })
  }

}

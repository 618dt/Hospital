import request from '@/utils/request'

export default {
    //分页条件查询
    getHospitalSetList(current, limit, searchObj){
        return request({
            //这里的地址即后端开发方法时定义的接口，复制过来即可,通过模板字符串将传入的参数值获取然后作为url路径
            url:`/admin/hosp/hospitalSet/findPage/${current}/${limit}`,
            method:'post', //提交方式与后端一致
            data: searchObj //使用json形式传递对象,如果是其他参数不使用json形式的话就用params
        })
    },
    //删除医院设置
    deleteHospitalSet(id) {
        return request ({
        url: `/admin/hosp/hospitalSet/${id}`,
        method: 'delete'
        })
    },
    //批量删除
    batchRemoveHospitalSet(idList) {
        return request({
        url: `/admin/hosp/hospitalSet/batchRemove`, 
        method: 'post',
        data: idList
          })
    },
    //锁定和取消锁定
    lockHospitalSet(id,status) {
        return request ({
        url: `/admin/hosp/hospitalSet/lockHospitalSet/${id}/${status}`,
        method: 'put'
        })
    },
    //添加医院设置
    saveHospSet(hospitalSet) {
        return request ({
        url: `/admin/hosp/hospitalSet/saveHospitalSet`,
        method: 'post',
        data: hospitalSet
        })
    },
    //根据id获取医院数据
    getHospitalSetById(id){
        return request({
            url: `/admin/hosp/hospitalSet/getHospitalSet/${id}`,
            method: 'get'
        })
    },
    //修改医院数据
    updateHospSet(hospitalSet){
        return request({
            url: `/admin/hosp/hospitalSet/updateHospitalSet`,
            method: 'post',
            data: hospitalSet
        })
    }

          
  
}
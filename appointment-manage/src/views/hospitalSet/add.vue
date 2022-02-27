<template>
  <div class="app-container">
      <el-form label-width="120px">
         <el-form-item label="医院名称">
            <el-input v-model="hospitalSet.hosname"/>
         </el-form-item>
         <el-form-item label="医院编号">
            <el-input v-model="hospitalSet.hoscode"/>
         </el-form-item>
         <el-form-item label="api基础路径">
            <el-input v-model="hospitalSet.apiUrl"/>
         </el-form-item>
         <el-form-item label="联系人姓名">
            <el-input v-model="hospitalSet.contactsName"/>
         </el-form-item>
         <el-form-item label="联系人手机">
            <el-input v-model="hospitalSet.contactsPhone"/>
         </el-form-item>
         <el-form-item inline="true" align="center">
            <el-button  type="primary" @click="saveOrUpdate">保存</el-button>
            <router-link :to="'/hospitalSet/list'">
               <el-button type="danger" >取消</el-button>
            </router-link>
         </el-form-item>
      </el-form>
   </div>
</template>

<script>

//引入接口定义的js文件
 import hospset from '@/api/hospitalSet.js'
    export default {
        data() {
          return {
            hospitalSet:{} //定义医院设置,初始值为空
          }
        },
        created() {
          //获取路由传来的id
          if(this.$route.params && this.$route.params.id){
            const id = this.$route.params.id
            this.getHospSetById(id)
          }else{//路由id不存在
            //表单数据清空
            this.hospitalSet={}
          }
        },
        methods: {
            //添加或修改
            saveOrUpdate() {
              if(this.hospitalSet.id){//id存在，修改操作
                  this.update()
              }else{
                this.save()
              }
            },
            //保存方法
            save(){
              hospset.saveHospSet(this.hospitalSet)//执行添加操作
                  .then(response => {
                    //提示
                    this.$message({
                        type: 'success',
                        message: '添加成功!'
                    })
                    //跳转到列表页面，使用路由跳转方式实现
                    this.$router.push({path:'/hospitalSet/list'})
                  })
            },
            //修改方法
            update(){
              hospset.updateHospSet(this.hospitalSet)//执行添加操作
                  .then(response => {
                    //提示
                    this.$message({
                        type: 'success',
                        message: '修改成功!'
                    })
                    //跳转到列表页面，使用路由跳转方式实现
                    this.$router.push({path:'/hospitalSet/list'})
                  })
            },
            //根据id获取数据
            getHospSetById(id){
              hospset.getHospitalSetById(id)
              .then(response =>{
                this.hospitalSet = response.data
              })
            }
        }
    }

</script>

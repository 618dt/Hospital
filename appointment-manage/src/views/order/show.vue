<template>
    <div class="app-container">
    <h4>订单信息</h4>
        <div class="info-wrapper" width="100%">
          <div class="info-form">
            <el-form ref="form" :model="form">
              <el-form-item label="就诊人信息：">
                <div class="content"><span>{{ orderInfo.patientName }}</span></div>
              </el-form-item>
              <el-form-item label="就诊日期：">
                <div class="content"><span>{{ orderInfo.reserveDate }} {{ orderInfo.reserveTime == 0 ? '上午' : '下午' }}</span></div>
              </el-form-item>
              <el-form-item label="就诊医院：">
                <div class="content"><span>{{ orderInfo.hosname }} </span></div>
              </el-form-item>
              <el-form-item label="就诊科室：">
                <div class="content"><span>{{ orderInfo.depname }} </span></div>
              </el-form-item>
              <el-form-item label="医生职称：">
                <div class="content"><span>{{ orderInfo.title }} </span></div>
              </el-form-item>
              <el-form-item label="医事服务费：">
                <div class="content">
                  <div class="fee">{{ orderInfo.amount }}元
                  </div>
                </div>
              </el-form-item>
              <el-form-item label="挂号单号：">
                <div class="content"><span>{{ orderInfo.outTradeNo }} </span></div>
              </el-form-item>
              <el-form-item label="挂号时间：">
                <div class="content"><span>{{ orderInfo.createTime }}</span></div>
              </el-form-item>
            </el-form>
          </div>
        </div>
    <br>
    <el-row>
      <el-button  @click="back">返回</el-button>
    </el-row>
    </div>
</template>

<script>
import statisticsApi from '@/api/sta'
export default {

  data() {
    return {
      //获取orderId, orderId/5 的形式
      orderId: this.$route.params.id,
      orderInfo: {
        param: {}
      }
    }
  },

  created() {
    this.init()
  },

  methods: {
    init() {
      statisticsApi.getOrderInfo(this.orderId).then(response => {
        console.log(response.data);
        this.orderInfo = response.data
      })
    },
    back() {
      window.history.back(-1)
    }
  }
}
</script>
<style>
  .info-wrapper {
    padding-left: 0;
    padding-top: 0;
  }
  .el-form-item {
    margin-bottom: 5px;
  }
</style>
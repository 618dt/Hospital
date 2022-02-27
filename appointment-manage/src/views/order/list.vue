<template>
    <div class="app-container">
        <!--查询表单-->
        <el-form :inline="true">
          <el-form-item label="就诊人：">
            <el-select v-model="searchObj.patientId" placeholder="请选择就诊人" class="v-select patient-select">
              <el-option
                v-for="item in patientList"
                :key="item.id"
                :label="item.name + '【' + item.certificatesNo + '】'"
                :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="订单状态：" style="margin-left: 80px">
            <el-select v-model="searchObj.orderStatus" placeholder="全部" class="v-select patient-select" style="width: 200px;">
              <el-option
                v-for="item in statusList"
                :key="item.status"
                :label="item.comment"
                :value="item.status">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="text" class="search-button v-link highlight clickable selected" @click="fetchData()">
              查询
            </el-button>
          </el-form-item>
        </el-form>

        <!-- 列表 -->
          <el-table
            :data="list"
            stripe
            style="width: 100%">
            <el-table-column
              label="就诊时间"
              width="120">
              <template slot-scope="scope">
                {{ scope.row.reserveDate }} {{ scope.row.reserveTime === 0 ? '上午' : '下午' }}
              </template>
            </el-table-column>
            <el-table-column
              prop="hosname"
              label="医院"
              width="100">
            </el-table-column>
            <el-table-column
              prop="depname"
              label="科室">
            </el-table-column>
            <el-table-column
              prop="title"
              label="医生职称">
            </el-table-column>
            <el-table-column
              prop="amount"
              label="医事服务费">
            </el-table-column>
            <el-table-column
              prop="patientName"
              label="就诊人">
            </el-table-column>
            <el-table-column
              prop="param.orderStatusString"
              label="订单状态">
            </el-table-column>
            <el-table-column label="操作">
              <template slot-scope="scope">
                    <router-link :to="'/order/show/'+scope.row.id">
                        <el-button type="primary" size="mini">详情</el-button>
                    </router-link>
              </template>
            </el-table-column>
          </el-table>

        <!-- 分页组件 -->
        <el-pagination
        :current-page="page"
        :total="total"
        :page-size="limit"
        :page-sizes="[5, 10, 20, 30, 40, 50, 100]"
        style="padding: 30px 0; text-align: center;"
        layout="sizes, prev, pager, next, jumper, ->, total, slot"
        @current-change="fetchData"
        @size-change="changeSize"
        />
    </div>
</template>

<script>
import statisticsApi from '@/api/sta'
export default {

    data() {
    return {
      list: [], // banner列表
      total: 0, // 数据库中的总记录数
      page: 1, // 默认页码
      limit: 10, // 每页记录数 
      searchObj: {// 查询表单对象
        patientId:'',
        orderStatus:''
      },  
      patientList: [],
      statusList: [{ //订单状态,数据写死在前端
          status: '0',
          comment: '预约成功,待支付'
        }, {
          status: '1',
          comment: '已支付'
        }, {
          status: '2',
          comment: '已取号'
        }, {
          status: '-1',
          comment: '取消预约'
        }] 
    }
  },

  created() {
    this.fetchData()
    this.findPatientList()
  },

  methods: {
    //初始化数据,显示订单列表
    fetchData(page = 1) {
      this.page = page
      statisticsApi.getPageList(this.page, this.limit, this.searchObj).then(response => {
        console.log(response.data);
        this.list = response.data.records
        this.total = response.data.total
      })
    },
    //就诊人列表
    findPatientList() {
      statisticsApi.findList().then(response => {
        this.patientList = response.data
      })
    },
    //页码改变
    changeSize(size) {
      console.log(size)
      this.limit = size
      this.fetchData(1)
    }
  }
}
</script>

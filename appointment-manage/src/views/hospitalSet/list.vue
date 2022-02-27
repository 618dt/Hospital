<template>
  <div class="app-container">
      <!--表单查询 -->
        <el-form :inline="true" class="demo-form-inline">
            <el-form-item>
                <el-input  v-model="searchObj.hosname" placeholder="医院名称"/>
            </el-form-item>
            <el-form-item>
                <el-input v-model="searchObj.hoscode" placeholder="医院编号"/>
            </el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="getList()">查询</el-button>
        </el-form>
      <!-- 工具条 -->
        <div>
        <el-button type="danger" size="mini" @click="removeRows()">批量删除</el-button>
        </div>
        <!-- banner列表 遍历整个list集合 -->
        <el-table :data="list"  stripe style="width: 100%" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55"/>  <!-- 复选框 -->
            <el-table-column type="index" width="50" label="序号"/>
            <el-table-column prop="hosname" label="医院名称"/>
            <el-table-column prop="hoscode" label="医院编号"/>
            <el-table-column prop="apiUrl" label="api基础路径" width="200"/>
            <el-table-column prop="contactsName" label="联系人姓名"/>
            <el-table-column prop="contactsPhone" label="联系人手机"/>
            <el-table-column label="状态" width="80">
                <template slot-scope="scope">  
                        {{ scope.row.status === 1 ? '可用' : '不可用' }}  <!--判断每一行数据的状态是否可用 '==='表示值和类型都相同 -->
                </template>
            </el-table-column>
            <el-table-column label="操作" width="280" align="center">
                <template slot-scope="scope">
                    <el-button type="danger" size="mini" 
                        icon="el-icon-delete" @click="removeDataById(scope.row.id,scope.row.hosname)">
                        删除
                    </el-button>
                    <el-button v-if="scope.row.status==1" type="primary" size="mini" 
                       icon="el-icon-delete" @click="lockHospSet(scope.row.id,0)">锁定</el-button>
                    <el-button v-if="scope.row.status==0" type="danger" size="mini" 
                       icon="el-icon-delete" @click="lockHospSet(scope.row.id,1)">解锁</el-button>
                    <router-link :to="'/hospitalSet/edit/'+scope.row.id">
                        <el-button type="primary" size="mini" icon="el-icon-edit">修改</el-button>
                    </router-link>
                </template>
            </el-table-column>
        </el-table>
        <!-- 分页 -->
        <el-pagination align='center'
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="current"
            :page-sizes="[2, 3, 5, 10]"
            :page-size="limit"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
        </el-pagination>
  </div>
</template>
<script>
//引入接口定义的js文件
import hospitalSet from '@/api/hospitalSet.js'

export default {
    //定义变量和初始值
    data(){
        return{
            current:1, //当前页，初始值为1
            limit:3, //每页记录数
            searchObj:{}, //条件封装对象
            list:[], //每页数据集合
            total:0, //总记录数
            multipleSelection: [] // 批量选择中选择的记录列表
        }
    },
    created(){ //在页面渲染之前执行
        //一般调用methods定义的方法，得到数据
        this.getList() //this,当前vue对象

    },
    methods:{//定义方法,进行请求接口调用
        //处理每一记录数变化
        handleSizeChange(limit = 3){
            this.limit = limit
            //console.log("每页显示为:"+limit)
            this.getList()
        },
        //处理当前页变化
        handleCurrentChange(page = 1){
            this.current = page
            this.getList()
        },
        //医院设置列表方法
        getList(){
            //调用引入的js文件里的方法获取列表
            hospitalSet.getHospitalSetList(this.current,this.limit,this.searchObj)
            .then( response => {//response是接口返回数据
                //返回集合复制给list
                this.list = response.data.records //根据json格式数据进行提取
                //返回总记录数
                this.total = response.data.total
            }) //请求成功
            .catch(error => {
                console.log(error)
            }) //请求失败
        },
        //删除医院设置的方法
        removeDataById(id,name) {
        this.$confirm('将要删除['+name+']的医院设置信息,是否继续?', '提示', {
            cancelButtonText: '取消',
            confirmButtonText: '确定',
            type: 'warning'
        }).then(() => { //确定执行then方法
            //调用接口
            hospitalSet.deleteHospitalSet(id)
                .then(response => {
                    //提示信息
                    this.$message({
                    type: 'success',
                    message: '删除成功!'
                    })
                    //刷新页面
                    this.getList(1)
                })
        })
        },
        //获取复选框选中的数据对象
        handleSelectionChange(selection){
            this.multipleSelection = selection
        },
        //批量删除
        removeRows() {
        var length = this.multipleSelection.length
        this.$confirm('将要删除'+length+'条医院设置信息, 是否继续?', '提示', {
            cancelButtonText: '取消',
            confirmButtonText: '确定',
            type: 'warning'
        }).then(() => { //确定执行then方法
            var idList = []
            //遍历对象数组得到每个id值，设置到idList里面
            for(var i=0;i<length;i++) {
                var obj = this.multipleSelection[i]//得到每个对象
                var id = obj.id //获取对象的id值;
                idList.push(id)
            }
            //调用接口
            hospitalSet.batchRemoveHospitalSet(idList)
                .then(response => {
                    //提示
                    this.$message({
                    type: 'success',
                    message: '删除成功!'
                    })
                    //刷新页面
                    this.getList(1)
                })
        })
        },
        //锁定和取消锁定
        lockHospSet(id,status) {
            hospitalSet.lockHospitalSet(id,status)
            .then(response => {
                //刷新
                this.getList()
            })
        }
    }
}
</script>

<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单号" prop="orderId">
        <el-input
          v-model="queryParams.orderId"
          placeholder="请输入订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="店铺" prop="shopId">
        <el-select v-model="queryParams.shopId" placeholder="请选择店铺" clearable @change="handleQuery">
         <el-option
            v-for="item in shopList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="下单时间" prop="orderTime">
        <el-date-picker clearable
                        v-model="orderTime" value-format="yyyy-MM-dd"
                        type="daterange"
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="订单状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable @change="handleQuery">
          <el-option label="待付款" value="10" ></el-option>
          <el-option label="待发货" value="20"></el-option>
          <el-option label="部分发货" value="21"> </el-option>
          <el-option label="待收货" value="30"></el-option>
          <el-option label="完成" value="100"></el-option>
          <el-option label="售后之后订单取消" value="200"></el-option>
          <el-option label="未付款订单取消" value="250"></el-option>
        </el-select>
      </el-form-item>
<!--      <el-form-item label="下单日期" prop="orderCreateTime">-->
<!--        <el-date-picker clearable-->
<!--          v-model="queryParams.orderCreateTime"-->
<!--          type="date"-->
<!--          value-format="yyyy-MM-dd"-->
<!--          placeholder="请选择订单创建时间">-->
<!--        </el-date-picker>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="订单状态" prop="statusStr">-->
<!--        <el-input-->
<!--          v-model="queryParams.statusStr"-->
<!--          placeholder="请输入订单状态"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">

      <el-col :span="1.5">
        <el-button
          :loading="pullLoading"
          type="success"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handlePull"
        >API拉取订单</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-refresh"
          size="mini"
          :disabled="multiple"
          @click="handleConfirm"
        >重新推送选中订单到订单库</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          icon="el-icon-refresh"-->
<!--          size="mini"-->
<!--          :disabled="multiple"-->
<!--          @click="handleConfirm"-->
<!--        >批量确认订单</el-button>-->
<!--      </el-col>-->

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange" >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="订单号" align="left" prop="orderId" width="200px">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetail(scope.row)"
          >{{scope.row.orderId}} </el-button>
          <i class="el-icon-copy-document tag-copy" :data-clipboard-text="scope.row.orderId" @click="copyActiveCode($event,scope.row.orderId)" ></i>
          <br/>
          <el-tag type="info">{{ shopList.find(x=>x.id == scope.row.shopId) ? shopList.find(x=>x.id == scope.row.shopId).name : '' }}</el-tag>
        </template>>
      </el-table-column>
<!--      <el-table-column label="订单号" align="center" prop="orderId" />-->
<!--      <el-table-column label="店铺" align="center" prop="shopId" >-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ shopList.find(x=>x.id == scope.row.shopId)?shopList.find(x=>x.id == scope.row.shopId).name :'' }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="商品" width="350">
          <template slot-scope="scope">
            <el-row v-for="item in scope.row.items" :key="item.id" :gutter="20">

            <div style="float: left;display: flex;align-items: center;" >
              <el-image  style="width: 50px; height: 50px;" :src="item.thumbImg"></el-image>
              <div style="margin-left:10px">
              <p>{{item.title}}</p>
              <p>
                <span>规格： </span>
                <el-tag size="small">{{ getSkuValues(item.skuAttrs)}}</el-tag>
                &nbsp;
                <span>数量： </span>
                <el-tag size="small" type="danger">{{item.skuCnt}}</el-tag>
                <span>SkuId：{{item.erpSkuId}} </span>

              </p>

              </div>
            </div>
            </el-row>
          </template>
      </el-table-column>
      <el-table-column label="订单金额" align="center" prop="orderPrice" :formatter="amountFormatter">
        <template slot-scope="scope">
          <span>
          {{ amountFormatter(null,null,scope.row.orderPrice/100,0) }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="订单创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{m}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="收件人" align="center" prop="userName" />
      <el-table-column label="省市区" align="center" >
        <template slot-scope="scope">
        <el-tag size="small" v-if="scope.row.provinceName">{{scope.row.provinceName}}</el-tag>
        <el-tag size="small" v-if="scope.row.cityName">{{scope.row.cityName}}</el-tag>
        <el-tag size="small" v-if="scope.row.countyName">{{scope.row.countyName}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="订单备注" align="center" >
        <template slot-scope="scope">
          <div style="color: #ed5565">{{scope.row.remark}}</div>
          <div style="color: #ed5565">{{scope.row.buyerMemo}}</div>
          <div style="color: #ed5565">{{scope.row.sellerMemo}}</div>
        </template>
      </el-table-column>
      <el-table-column label="订单状态" align="center" prop="status" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 10 " size="small">待付款</el-tag>
          <el-tag v-if="scope.row.status === 11 " size="small">已退款</el-tag>
          <el-tag v-if="scope.row.status === 20 " size="small">待发货</el-tag>
          <el-tag v-if="scope.row.status === 30 " size="small">待收货</el-tag>
          <el-tag v-if="scope.row.status === 100 " size="small">完成</el-tag>
          <el-tag v-if="scope.row.status === 250 " size="small">未付款超时取消</el-tag>
          <br/>
<!--          <el-tag style="margin-top: 5px" type="warning" v-if="scope.row.confirmStatus === 0 " size="small">待确认</el-tag>-->
        </template>
      </el-table-column>
<!--      <el-table-column label="tenant" align="center" prop="tenantId" v-if="isAdmin"/>-->
<!--      <el-table-column label="快递单号" align="center" prop="logisticsCode" />-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type=""
            icon="el-icon-refresh"
            :loading="pullLoading"
            @click="handlePullUpdate(scope.row)"
          >更新订单</el-button>

        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

  </div>
</template>

<script>

import { listShop } from "@/api/shop/shop";
import { searchSku } from "@/api/goods/goods";
import {MessageBox} from "element-ui";
import {isRelogin} from "../../../../utils/request";
import {listShopOrder, pullOrder, orderConfirm, pullOrderDetail} from "@/api/shop/shop_order";
import Clipboard from "clipboard";
import {getUserProfile} from "@/api/system/user";

export default {
  name: "OrderPdd",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      pullLoading: false,
      isAdmin: false,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      multiple: true,
      // 总条数
      total: 0,
      // 淘宝订单表格数据
      orderList: [],
      shopList:[],
      orderTime:null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        shopId: null,
        shopType: 3,
        status: null
      },
      // 表单参数
      form: {
      },
      rules: {
      }
    };
  },
  created() {
    getUserProfile().then(resp=>{
      this.isAdmin = resp.data.admin
    })
    listShop({type:3}).then(response => {
        this.shopList = response.rows;
      if (this.shopList && this.shopList.length > 0) {
        this.queryParams.shopId = this.shopList[0].id
      }
      this.getList();
      });

  },
  methods: {
    copyActiveCode(event,queryParams) {
      console.log(queryParams)
      const clipboard = new Clipboard(".tag-copy")
      clipboard.on('success', e => {
        this.$message({ type: 'success', message: '复制成功' })
        // 释放内存
        clipboard.destroy()
      })
      clipboard.on('error', e => {
        // 不支持复制
        this.$message({ type: 'waning', message: '该浏览器不支持自动复制' })
        // 释放内存
        clipboard.destroy()
      })
    },
    amountFormatter(row, column, cellValue, index) {
      return '￥' + cellValue.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
    },
    getSkuValues(spec){
      try {
        // 解析 JSON，返回一个数组
        const parsedSpec = JSON.parse(spec) || [];

        // 使用 map 提取所有 value，使用 join() 用逗号连接
        return parsedSpec.map(item => item.attr_value || item.value).join(', ') || '';
      } catch (error) {
        return spec; // 如果 JSON 解析出错，返回空字符串
      }
    },
    /** 查询淘宝订单列表 */
    getList() {
      this.loading = true;
      if(this.orderTime){
        this.queryParams.startTime = this.orderTime[0]
        this.queryParams.endTime = this.orderTime[1]
      }else {
        this.queryParams.startTime = null
        this.queryParams.endTime = null
      }
      listShopOrder(this.queryParams).then(response => {
        this.orderList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.detailOpen = false;
      this.saleAfterOpen = false
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        shopId: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },

    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },

    handlePull() {
      if(this.queryParams.shopId){
        this.pullLoading = true
        pullOrder({shopId:this.queryParams.shopId}).then(response => {
          console.log('拉取订单接口返回=====',response)
          if(response.code === 200){
            this.$modal.msgSuccess(JSON.stringify(response));
            this.pullLoading = false
            this.getList()
          }
          else if(response.code === 1401) {
            MessageBox.confirm('Token已过期，需要重新授权！请前往店铺列表重新获取授权！', '系统提示', { confirmButtonText: '前往授权', cancelButtonText: '取消', type: 'warning' }).then(() => {
              this.$router.push({path:"/shop/list",query:{type:3}})
            }).catch(() => {
              isRelogin.show = false;
            });
          }else {
            this.$modal.msgError(JSON.stringify(response));
            this.pullLoading = false
          }

        })
      }else{
        this.$modal.msgSuccess("请先选择店铺");
      }

      // this.$modal.msgSuccess("请先配置API");
    },
    handlePullUpdate(row) {
      // 接口拉取订单并更新
      this.pullLoading = true
      pullOrderDetail({shopId:row.shopId,orderId:row.orderId}).then(response => {
        console.log('拉取订单详情返回接口返回=====',response)
        // if(response.code>1000){
        //   this.$modal.msgError(response.msg);
        // }else{
        //   this.$modal.msgSuccess(JSON.stringify(response));
        //   this.pullLoading = false
        //   this.getList()
        // }
        if(response.code === 200){
          this.$modal.msgSuccess(JSON.stringify(response));
          this.pullLoading = false
          this.getList()
        }
        else if(response.code === 1401) {
          MessageBox.confirm('Token已过期，需要重新授权！请前往店铺列表重新获取授权！', '系统提示', { confirmButtonText: '前往授权', cancelButtonText: '取消', type: 'warning' }).then(() => {
            this.$router.push({path:"/shop/list",query:{type:3}})
          }).catch(() => {
            isRelogin.show = false;
          });
        }else {
          this.$modal.msgError(JSON.stringify(response));
          this.pullLoading = false
        }

      })
    },
    handleConfirm(row) {
      const ids = row.id || this.ids;
      console.log('批量确认订单:',ids)
      this.$modal.confirm('是否重新推送订单到订单库？').then(function() {
        return orderConfirm({ids:ids});
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("推送成功");
      }).catch(() => {});
    },
  }
};
</script>

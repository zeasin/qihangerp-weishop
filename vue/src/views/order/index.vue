<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单编号" prop="orderNum">
        <el-input
          v-model="queryParams.orderNum"
          placeholder="请输入订单编号"
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
            <span style="float: left">{{ item.name }}</span>


              <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 9">线下渠道</span>
              <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 3">拼多多</span>
              <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 5">微信小店</span>
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
      <el-form-item label="订单状态" prop="orderStatus">
        <el-select v-model="queryParams.orderStatus" placeholder="请选择状态" clearable @change="handleQuery">
          <el-option label="待发货" value="1" ></el-option>
          <el-option label="已发货" value="2"></el-option>
          <el-option label="已完成" value="3"> </el-option>
          <el-option label="已取消" value="11"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          icon="el-icon-refresh"-->
<!--          size="mini"-->
<!--          @click="handleCreate"-->
<!--        >手动创建订单</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          icon="el-icon-refresh"-->
<!--          size="mini"-->
<!--          :disabled="multiple"-->
<!--          @click="handleShip"-->
<!--        >批量分配发货</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['shop:order:export']"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
<!--      <el-table-column type="selection" width="55" align="center"  :selectable="isRowSelectable" />-->
<!--      <el-table-column label="订单ID" align="center" prop="id" />-->
      <el-table-column label="订单号" align="left" prop="orderNum" width="200px">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetail(scope.row)"
          >{{scope.row.orderNum}} </el-button>
          <i class="el-icon-copy-document tag-copy" :data-clipboard-text="scope.row.orderNum" @click="copyActiveCode($event,scope.row.orderNum)" ></i>
          <br/>
          <el-tag type="info">{{ shopList.find(x=>x.id == scope.row.shopId) ? shopList.find(x=>x.id == scope.row.shopId).name : '' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="商品" width="400">
          <template slot-scope="scope">
            <el-row v-for="item in scope.row.itemList" :key="item.id" :gutter="20">
            <div style="float: left;display: flex;align-items: center;" >
              <el-image  style="width: 50px; height: 50px;" :src="item.goodsImg"></el-image>
              <div style="margin-left:10px">
              <p>{{item.goodsTitle}}</p>
              <p>
<!--                <div v-if="JSON.parse(item.goodsSpec)">-->
<!--                {{JSON.parse(item.goodsSpec)[0].attr_key}}&nbsp;： {{JSON.parse(item.goodsSpec)[0].attr_value}}&nbsp;-->
<!--                </div>-->
<!--                <div v-else-if="JSON.parse(item.goodsSpec).length>1">-->
<!--                {{JSON.parse(item.goodsSpec)[1].attr_key}}&nbsp;： {{JSON.parse(item.goodsSpec)[1].attr_value}}&nbsp;-->
<!--                </div>-->
                <div>
                {{ getSkuValues(item.goodsSpec)}}
                数量：<el-tag size="small">x {{item.quantity}}</el-tag>
                </div>
                </p>
              </div>
            </div>
            </el-row>
          </template>
      </el-table-column>
      <el-table-column label="订单金额" align="center" prop="orderAmount" :formatter="amountFormatter"/>
      <el-table-column label="订单备注" align="center" >
        <template slot-scope="scope">
          <div style="color: #ed5565">{{scope.row.remark}}</div>
          <div style="color: #ed5565">{{scope.row.buyerMemo}}</div>
          <div style="color: #ed5565">{{scope.row.sellerMemo}}</div>
        </template>
      </el-table-column>
      <el-table-column label="发货状态" align="center" prop="shipStatus" >
        <template slot-scope="scope">
          <el-tag type="info" v-if="scope.row.shipStatus === 0" style="margin-bottom: 6px;">待发货</el-tag>
          <el-tag type="info" v-if="scope.row.shipStatus === 1" style="margin-bottom: 6px;">部分发货</el-tag>
          <el-tag type="info" v-if="scope.row.shipStatus === 2" style="margin-bottom: 6px;">全部发货</el-tag>
          <br />
          <!-- 1：无售后或售后关闭，2：售后处理中，3：退款中，4： 退款成功 -->
          <el-tag v-if="scope.row.shipType === 0" type="info">自己发货</el-tag>
          <el-tag v-if="scope.row.shipType === 1" type="info">联合发货</el-tag>
          <el-tag v-if="scope.row.shipType === 2" type="info">供应商发货</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="订单状态" align="center" prop="orderStatus" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.orderStatus === 0" style="margin-bottom: 6px;">待选择发货方式</el-tag>
          <el-tag v-if="scope.row.orderStatus === 1" style="margin-bottom: 6px;">待发货</el-tag>
          <el-tag v-if="scope.row.orderStatus === 2" style="margin-bottom: 6px;">已发货</el-tag>
          <el-tag v-if="scope.row.orderStatus === 3" style="margin-bottom: 6px;">已发货</el-tag>
          <el-tag v-if="scope.row.orderStatus === 4" style="margin-bottom: 6px;">已完成</el-tag>
          <el-tag v-if="scope.row.orderStatus === 11" style="margin-bottom: 6px;">订单取消</el-tag>
          <br />
           <!-- 1：无售后或售后关闭，2：售后处理中，3：退款中，4： 退款成功 -->
           <el-tag v-if="scope.row.refundStatus === 1">无售后或售后关闭</el-tag>
           <el-tag v-if="scope.row.refundStatus === 2">售后处理中</el-tag>
           <el-tag v-if="scope.row.refundStatus === 3">退款中</el-tag>
           <el-tag v-if="scope.row.refundStatus === 4">退款成功</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="收件信息" prop="receiverName" >
        <template slot-scope="scope">
          {{scope.row.receiverName}}&nbsp;  {{scope.row.receiverMobile}} <br />
          {{scope.row.province}} {{scope.row.city}} {{scope.row.town}}
        </template>
      </el-table-column>
      <el-table-column label="下单时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.orderTime) }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetail(scope.row)"
          >详情</el-button>
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

    <!-- 订单详情对话框 -->
    <el-dialog :title="detailTitle" :visible.sync="detailOpen" width="1100px" append-to-body>

      <el-form ref="form" :model="form" :rules="rules" label-width="80px" inline>
        <el-descriptions title="订单信息">
            <el-descriptions-item label="ID">{{form.id}}</el-descriptions-item>
            <el-descriptions-item label="订单号">{{form.orderNum}}</el-descriptions-item>

            <el-descriptions-item label="店铺">
              {{ shopList.find(x=>x.id === form.shopId)?shopList.find(x=>x.id === form.shopId).name:'' }}
              <el-tag size="small" v-if="form.shopType === 5">视频号小店</el-tag>
            </el-descriptions-item>


            <el-descriptions-item label="标签">
              <el-tag size="small" v-if="form.tag ==='1' ">实售</el-tag>
              <el-tag size="small" v-if="form.tag ==='2' ">淘宝客</el-tag>
              <el-tag size="small" v-if="form.tag ==='3' ">刷单</el-tag>
              <el-tag size="small" v-if="form.tag ==='4' ">返现</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="买家留言">
              {{form.buyerMemo}}
            </el-descriptions-item>
            <el-descriptions-item label="备注">
              {{form.remark}}
            </el-descriptions-item>
            <el-descriptions-item label="创建时间">
              {{ form.createTime }}
              <!-- <el-date-picker
              disabled
                v-model="form.orderCreateTime"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="请选择订单创建时间">
              </el-date-picker> -->
            </el-descriptions-item>
             <el-descriptions-item label="支付时间"> {{ form.payTime }}</el-descriptions-item>
             <el-descriptions-item label="最后更新时间"> {{ form.updateTime }}</el-descriptions-item>

            <el-descriptions-item label="订单状态">
              <el-tag v-if="form.orderStatus === 1" style="margin-bottom: 6px;">待发货</el-tag>
              <el-tag v-if="form.orderStatus === 2" style="margin-bottom: 6px;">已发货</el-tag>
              <el-tag v-if="form.orderStatus === 3" style="margin-bottom: 6px;">已签收</el-tag>
              <el-tag v-if="form.orderStatus === 11" style="margin-bottom: 6px;">订单取消</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="退款状态">
              <el-tag v-if="form.refundStatus === 1">无售后或售后关闭</el-tag>
              <el-tag v-if="form.refundStatus === 2">售后处理中</el-tag>
               <el-tag v-if="form.refundStatus === 3">退款中</el-tag>
               <el-tag v-if="form.refundStatus === 4">退款成功</el-tag>
            </el-descriptions-item>

        </el-descriptions>
        <el-descriptions title="付款信息">
            <el-descriptions-item label="商品总额">{{form.goodsAmount}}</el-descriptions-item>
            <el-descriptions-item label="优惠金额">{{form.discountAmount}}</el-descriptions-item>
            <el-descriptions-item label="运费">{{form.postage}}</el-descriptions-item>
            <el-descriptions-item label="实际支付金额">{{form.amount}}</el-descriptions-item>
        </el-descriptions>


         <el-descriptions title="收货信息">
          <el-descriptions-item label="收件人姓名">{{form.receiverName}}</el-descriptions-item>
          <el-descriptions-item label="收件人手机号">{{form.receiverPhone}}</el-descriptions-item>
          <el-descriptions-item label="省市区">{{form.province}}{{form.city}}{{form.town}}</el-descriptions-item>
          <el-descriptions-item label="详细地址">{{form.address}}</el-descriptions-item>
        </el-descriptions>
        <el-descriptions title="发货信息">
          <el-descriptions-item label="发货方式">
            <el-tag v-if="form.shipType === 1"  type="danger">供应商代发</el-tag>
              <el-tag v-if="form.shipType === 0" type="danger">仓库发货</el-tag>
          </el-descriptions-item>
            <el-descriptions-item label="物流公司">{{form.shippingCompany}}</el-descriptions-item>
            <el-descriptions-item label="物流单号">{{form.shippingNumber}}</el-descriptions-item>
            <el-descriptions-item label="发货时间">{{form.shippingTime}}</el-descriptions-item>
        </el-descriptions>

        <el-divider content-position="center">订单商品</el-divider>
        <el-table :data="form.erpOrderItemList"  style="margin-bottom: 10px;">
          <!-- <el-table-column type="selection" width="50" align="center" /> -->
          <el-table-column label="序号" align="center" type="index" width="50"/>

          <el-table-column label="商品图片" prop="goodsImg" width="80">
            <template slot-scope="scope">
              <el-image style="width: 70px; height: 70px" :src="scope.row.goodsImg"></el-image>
            </template>
          </el-table-column>
          <el-table-column label="商品标题" prop="goodsTitle" ></el-table-column>
          <el-table-column label="SKU" prop="goodsSpec" width="150"></el-table-column>
          <el-table-column label="sku编码" prop="specNum"></el-table-column>
          <el-table-column label="单价" prop="goodsPrice"></el-table-column>
          <el-table-column label="数量" prop="quantity"></el-table-column>
          <!-- <el-table-column label="商品金额" prop="itemAmount"></el-table-column> -->
        </el-table>
      </el-form>
      <!-- <div slot="footer" class="dialog-footer" v-if="isAudit">
        <el-button type="primary" @click="submitConfirmForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div> -->
    </el-dialog>
    <!-- 订单发货确认对话框 -->
    <el-dialog title="订单发货确认" :visible.sync="shipConfirmOpen" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="108px">
        <el-form-item label="订单编号" prop="orderIds">
          <el-col :span="24">
            <el-input v-model="form.orderIds" style="width:220px" placeholder="请选择订单" disabled />
          </el-col>

        </el-form-item>
        <el-form-item label="发货方式" prop="shipType">
          <el-select v-model="form.shipType" placeholder="请选择发货方式" >
            <el-option  label="仓库发货" value="0"></el-option>
            <el-option  label="供应商发货" value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="备注内容" style="width: 220px;" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" style="margin-left: 108px;">
        <el-button type="primary" @click="submitForm">确认发货</el-button>
         <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listOrder, getOrder, delOrder, addOrder, updateOrder, shipOrder} from "@/api/order/order";
import { listShop } from "@/api/shop/shop";
import Clipboard from "clipboard";
import {getUserProfile} from "@/api/system/user";

export default {
  name: "Order",
  data() {
    return {
      // 遮罩层
      loading: true,
      isAdmin: false,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedSShopOrderItem: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      orderTime: null,
      // 显示搜索条件
      showSearch: true,
      shipConfirmOpen: false,
      // 总条数
      total: 0,
      // 店铺订单表格数据
      orderList: [],
      // ${subTable.functionName}表格数据
      sShopOrderItemList: [],
      shopList:[],
      // 弹出层标题
      detailTitle:'订单详情',
      detailOpen:false,
      isAudit:false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNum: null,
        shopId: null,
        startTime:null,
        endTime:null
      },
      // 表单参数
      form: {
        orderIds:null,
        shipType:null,
        remark:null
      },
      // 表单校验
      rules: {
        orderIds: [{ required: true, message: '不能为空' }],
        shipType: [{ required: true, message: '请选择发货方式' }],
      }
    };
  },
  created() {
    getUserProfile().then(resp=>{
      this.isAdmin = resp.data.admin
    })
     listShop({}).then(response => {
        this.shopList = response.rows;
      });
    this.getList();
  },
  methods: {
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
    amountFormatter(row, column, cellValue, index) {
      return '￥' + cellValue.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
    },
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

    /** 查询店铺订单列表 */
    getList() {
      this.loading = true;
      if(this.orderTime){
        this.queryParams.startTime = this.orderTime[0]
        this.queryParams.endTime = this.orderTime[1]
      }else {
        this.queryParams.startTime = null
        this.queryParams.endTime = null
      }
      listOrder(this.queryParams).then(response => {
        this.orderList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
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
    isRowSelectable(row, index) {
      return row.orderStatus === 1 ;
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    reset(){

    },
    handleShip(row){
      const id = row.id || this.ids
      console.log('批量确认发货',id)
      this.form.orderIds = id
      this.shipConfirmOpen = true
    },
    /** 详情按钮操作 */
    handleDetail(row) {
      this.reset();
      const id = row.id || this.ids
      getOrder(id).then(response => {
        this.form = response.data;
        // this.$nextTick(()=>{
        //   this.form.shipType = response.data.shipType
        // })
        this.detailOpen = true;
        this.detailTitle = "订单详情";
      });
      this.isAudit = false
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          shipOrder(this.form)
        }
      })
    },
    handleCreate(){
      this.$router.push("/order/create")
    },
    cancel(){
      this.shipConfirmOpen = false
    }
  }
};
</script>

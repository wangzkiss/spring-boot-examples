<%@ page contentType="text/html;charset=UTF-8" %>
  <%@ include file="/webpage/include/taglib.jsp"%>
    <html>

    <head>
      <title>首页</title>
      <meta name="decorator" content="default" />
      <script src="${ctxStatic}/echarts/echarts.min.js" type="text/javascript"></script>
      <link rel="stylesheet" type="text/css" href="${ctxStatic}/iview/iview.css">
      <!-- <link rel="stylesheet" type="text/css" href="${ctxStatic}/iview/fonts/ionicons.ttf">
      <link rel="stylesheet" type="text/css" href="${ctxStatic}/iview/fonts/ionicons.woff"> -->
      <script src="${ctxStatic}/vue.js"></script>
      <script src="${ctxStatic}/iview/iview.min.js"></script>
      <style type="text/css">
      [v-cloak]{
        display: none;
      }
      #chart>div {
        padding: 10px 7px;
      }
      
      #chart .chartDiv {
        border: 1px solid #dedfe3;
        height: 154px;
      }
      
      #chart .chartNum {
        position: relative;
      }
      
      #chart .chartNum,
      #chart .chartNum2 {
        border: 1px solid #dedfe3;
        height: 154px;
        color: #57c202;
        font-size: 40px;
        font-family: '微软雅黑';
        font-weight: 900;
        text-align: center;
        line-height: 190px;
      }
      #chart .chartzw{
        /*border: 1px solid #dedfe3;*/
        height: 120px;
        color: #57c202;
        font-size: 16px;
        font-family: '微软雅黑';
        font-weight: 400;
        text-align: center;
        line-height: 110px;
      }
      
      .chartNum .chartName {
        position: absolute;
      }
      .chartNum:hover{
        /*box-shadow: -4px 4px 2px rgba(0,0,0, .1);*/
        box-shadow: 0 1px 6px rgba(0,0,0,.3);
        transition: all .2s ease-in-out;
      }
      
      .chartNum2 .chartName {
        margin: 16px 0;
        font-size: 12px;
      }
      
      .chartName {
        width: 100%;
        font-size: 14px;
        line-height: 12px;
        top: 20px;
        color: #252525;
      }
      
      #chart .chartNum2 {
        font-size: 22px;
        line-height: 10px;
      }
      
      #pirChart {
        margin-top: 40px;
      }
      
      .userBox {
        padding: 17px;
      }
      
      .userPic img {
        width: 112px;
        height: 90px;
      }
      
      .rightMes {
        margin-left: 10px;
      }
      
      .rightMes a {
        margin-left: 4px;
      }
      
      .rightMes p:first-child {
        margin-bottom: 14px;
      }
      #chart .chartNum, #chart .chartNum2{font-weight: 400;}
      .demo-badge{
          width: 42px;
          height: 42px;
          background: #eee;
          border-radius: 6px;
          display: inline-block;
      }
      </style>
      <script src="${ctxStatic}/radialIndicator/radialIndicator.min.js" type="text/javascript"></script>
      <script type="text/javascript">
      $(document).ready(function() {

        option = {
          tooltip: {
            trigger: 'axis',
            position: function(pt) {
              return [pt[0], '20%'];
            }
          },
          grid: {
            left: '12%',
            right: '12%',
            bottom: '0%',
            top: '22%',
            containLabel: true
          },
          title: {
            text: '折线图堆叠',
            top: '6',
            left: 'center',
            textStyle: {
              fontFamily: 'Arial',
              fontWeight: 600,
              fontSize: 2
            }
          },
          xAxis: {
            type: 'category',
            boundaryGap: false,
            axisLabel: {
              show: false
            },
            axisTick: {
              show: false
            },
            axisLine: {
              show: false
            },
            splitLine: {
              show: true,
              interval: 2,
              lineStyle: {
                type: 'dashed'
              }
            },
            data: []
          },
          yAxis: {
            type: 'value',
            splitNumber: 2,
            axisTick: {
              inside: true
            },
            splitLine: {
              show: true,
              lineStyle: {
                type: 'dashed'
              }
            },
            axisLabel: {
              formatter: '{value}',
              inside: true
            }
          },

          series: []
        };
        chartData = {
          name: '模拟数据',
          type: 'line',
          stack: '总量',
          data: [],
          showSymbol: false,
          itemStyle: {
            normal: {
              color: '#59c404'
            }
          },
        }

        var monitoring = {
          init: function() {
            var __this = this;

            __this.chartStart()
            __this.changePassWord()
          },
          // 时间戳转换 10位数字
          getLocalTime: function(nS) {
            return new Date(parseInt(nS) * 1000).toLocaleString().replace(/年|月/g, "-").replace(/日/g, " ");
          },

          //地址
          sendUrl: function() {
            urlDc = '${ctx}/server/platform/index';
            return urlDc
          },
          //对象深复制
          deepCopy: function clone(obj) {
            var o;
            switch (typeof obj) {
              case 'undefined':
                break;
              case 'string':
                o = obj + '';
                break;
              case 'number':
                o = obj - 0;
                break;
              case 'boolean':
                o = obj;
                break;
              case 'object':
                if (obj === null) {
                  o = null;
                } else {
                  if (obj instanceof Array) {
                    o = [];
                    for (var i = 0, len = obj.length; i < len; i++) {
                      o.push(clone(obj[i]));
                    }
                  } else {
                    o = {};
                    for (var k in obj) {
                      o[k] = clone(obj[k]);
                    }
                  }
                }
                break;
              default:
                o = obj;
                break;
            }
            return o;
          },
          changePassWord: function() {
            $("#userPassWordBtn").click(function() {
              top.layer.open({
                type: 2,
                area: ['600px', '350px'],
                title: "修改密码",
                content: "${ctx}/sys/user/modifyPwd",
                btn: ['确定', '关闭'],
                yes: function(index, layero) {
                  var body = top.layer.getChildFrame('body', index);
                  var inputForm = body.find('#inputForm');
                  var btn = body.find('#btnSubmit');
                  var top_iframe = top.getActiveTab().attr("name"); //获取当前active的tab的iframe 
                  inputForm.attr("target", top_iframe); //表单提交成功后，从服务器返回的url在当前tab中展示
                  inputForm.validate({
                    rules: {},
                    messages: {
                      confirmNewPassword: {
                        equalTo: "输入与上面相同的密码"
                      }
                    },
                    submitHandler: function(form) {
                      loading('正在提交，请稍等...');
                      form.submit();

                    },
                    errorContainer: "#messageBox",
                    errorPlacement: function(error, element) {
                      $("#messageBox").text("输入有误，请先更正。");
                      if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
                        error.appendTo(element.parent().parent());
                      } else {
                        error.insertAfter(element);
                      }
                    }
                  });
                  if (inputForm.valid()) {
                    loading("正在提交，请稍等...");
                    inputForm.submit();
                    top.layer.close(index); //关闭对话框。
                  } else {
                    return;
                  }


                },
                cancel: function(index) {}
              });
            });
          },
          //chart_start
          chartStart: function() {
            var __this = this,
              url = __this.sendUrl(), //get地址
              num = 0;


            $.ajax({
              type: "GET",
              url: url,
              dataType: 'json',
              async: false,
              success: function(json) {
                vm.info(json.JobInfo.pendingauditCount);
                vm.success(json.JobInfo.runingCount);
                Vue.nextTick(function (){
                  $("#ds").click(function(){
                    top.openTab('${ctx}/tji/job?opFlag=1','作业管理', false)
                  })
                  $("#yx").click(function(){
                    top.openTab('${ctx}/tji/job?opFlag=4','作业管理', false)
                  })
                })
                var HDFS_Disk = json.HDFS_Disk_Usage.metrics.dfs.FSNamesystem,
                  HDFS_Pie = (HDFS_Disk.CapacityTotalGB - HDFS_Disk.CapacityRemainingGB) / HDFS_Disk.CapacityTotalGB,
                  chartBox = $('<div class="col-xs-6 col-sm-4 col-md-3"></div>'),
                  chartBox2 = $('<div class="chartNum"></div>'),
                  timeBox = {
                    'Namenode&nbsp;运行时间': json.Namenode_Uptime.ServiceComponentInfo.StartTime,
                    'Resourcemanager&nbsp;运行时间': json.Resourcemanager_Uptime.ServiceComponentInfo.StartTime,
                    'Hbase master&nbsp;运行时间': json.Hbase_master_Uptime.ServiceComponentInfo.MasterStartTime
                  },
                  preBox = {
                    'Datanode&nbsp;状态': json.Datanode_Live.ServiceComponentInfo,
                    'Nodemanager&nbsp;状态': json.Nodemanager_Live.ServiceComponentInfo,
                    'Regionserver&nbsp;状态': json.Regionserver_Live.ServiceComponentInfo,
                    'Etlservers&nbsp;状态': json.Etlservers_Live.ServiceComponentInfo,
                  }
                var HDFS_Div = '<div class="chartName">HDFS 磁盘使用率</div><div id="pirChart"></div>';
                // $('.waitting span').html(json.JobInfo.pendingauditCount)
                // $('.running span').html(json.JobInfo.runingCount)
                
                chartBox2.append(HDFS_Div);
                chartBox.append(chartBox2);
                $('#chart').append(chartBox);
                HDFS_Pie = HDFS_Pie * 100;
                $('#pirChart').radialIndicator({
                  showPercentage: false,
                  radius: 42,
                  barWidth: 16,
                  barBgColor: '#eee',
                  barColor: '#5ab405',
                  fontColor: '#5ab405',
                  initValue: HDFS_Pie,
                  percentage: true,
                  fontFamily: '微软雅黑',
                  fontWeight: 900,
                  fontSize: 18
                });

                $.each(timeBox, function(i, n) {
                  var n = new Date().getTime() - n;
                  n = (n / (60 * 60 * 24 * 1000)).toFixed(0);
                  var numBox = $('<div class="chartName">' + i + '</div>' + n +'<font class="chartzw">天</font>'),
                    chartBox = $('<div class="col-xs-6 col-sm-4 col-md-3"></div>'),
                    chartBox2 = $('<div class="chartNum"></div>');
                  chartBox2.append(numBox);
                  chartBox.append(chartBox2);
                  $('#chart').append(chartBox);
                })

                $.each(preBox, function(i, n) {
                  var numBox = $('<div class="chartName">' + i + '</div>' + n.started_count + '/' + n.total_count),
                    chartBox = $('<div class="col-xs-6 col-sm-4 col-md-3"></div>'),
                    chartBox2 = $('<div class="chartNum"></div>');
                  chartBox2.append(numBox);
                  chartBox.append(chartBox2);
                  $('#chart').append(chartBox);
                  //started_count  total_count
                })

              }

            })


            return this;
          }

        }

        monitoring.init();
      })
      </script>
    </head>

    <body class="gray-bg" id="container">
      <div class="wrapper wrapper-content">
        <div class="panel panel-info" style=" border-color:#dedfe3;">
          <div class="panel-heading">今日作业报告</div>
          <div class="panel-body">
           <div class="row">
            <div class="col-md-4">
              <div id="pieChart" style="height: 240px;"></div>
            </div>
            <div class="col-md-8">
              <div id="chartDay" style="height: 240px;"></div>
            </div>
          </div>
          </div>
        </div>
        <div class="panel panel-info" style=" border-color:#dedfe3;">
          <div class="panel-heading">服务指标</div>
          <div class="panel-body">
            <div id="chart" class="clearfix"></div>
          </div>
        </div>
      </div>
      <script>
        
        var vm = new Vue({
          el: '#container',
          data: {
          },
          methods: {
            info (nodesc) {
              if(nodesc){
                this.$Notice.info({
                    title: nodesc ? '待审核作业 '+'<bold id="ds"><a href="javascript:void(0)">'+nodesc+'</a></bold> 条' : '没有数据提供',
                    duration: 12
                });
              }
            },
            success (nodesc) {
              if(nodesc){
                this.$Notice.success({
                    title: nodesc ? '正在运行作业 '+'<bold id="yx"><a href="javascript:void(0)">'+nodesc+'</a></bold> 条' : '没有数据提供',
                    duration: 12
                });
              }
            },
          },
          watch: {
          },
        })
        // 柱状图
        function initDay(type){
          var myChart1 = echarts.init(document.getElementById('chartDay'));    
          var myChart2 = echarts.init(document.getElementById('pieChart'));    
          var typeNameY = [],failCountX = [],successCountX = [], allSuccessSum = '', allFailSum = '';
          var url = '${ctx}/tji/jobschedule/report-json?flag='+type
          $.get(url, function(json){
            var data = JSON.parse(json).data
            // console.log(data)
            allFailSum = data.allFailSum
            allSuccessSum = data.allSuccessSum
            var typeReport = data.typeReport;
            $.each(typeReport, function(i, n){
              typeNameY.push(n.typeName)
              failCountX.push(n.failCount)
              successCountX.push(n.successCount)
            })
            var option = {
                tooltip: {
                    trigger: 'axis',
                    axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    },
                    formatter: function (params){
                      
                        return params[0].name + '<br/>'
                               + params[0].seriesName + ' : ' + params[0].value + '<br/>'
                               + params[1].seriesName + ' : ' + (params[1].value);
                    }
                },
                legend: {
                    data:['成功数','失败数']
                },
                grid: {
                  left: '3%',
                  right: '6%',
                  top: '20%',
                },
                toolbox: {
                    show: true,
                    orient: 'vertical',
                    y: 'center',
                },
                calculable: true,
                xAxis: [
                    {
                      type: 'category',
                      data: typeNameY,
                      axisLabel :{  
                      rotate: -25,
                      interval:0   
                  } 
                    }
                ],
                yAxis: [
                    {
                        type: 'value',
                        splitNumber: 2,
                        position:'right',
                    }
                ],
                series: [
                    {
                        name: '成功数',
                        type: 'bar',
                        data: successCountX,
                        itemStyle: {
                            normal: {
                                color: '#67beea',
                                barBorderColor: '#67beea',
                                barBorderWidth: 1,
                                barBorderRadius:0,
                                label : {
                                    show: true, 
                                    position: 'top',
                                    textStyle: {
                                        color: '#67beea'
                                    }
                                }
                            }
                        },
                    },
                    {
                        name: '失败数',
                        type: 'bar',
                        data: failCountX,
                        itemStyle: {
                            normal: {
                                color: 'tomato',
                                barBorderColor: 'tomato',
                                barBorderWidth: 1,
                                barBorderRadius:0,
                                label : {
                                    show: true, 
                                    position: 'top',
                                    textStyle: {
                                        color: '#FF6347'
                                    }
                                }
                            }
                        },
                    }
                ]
            };

            var option2 = {
                tooltip : {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {
                    orient: 'vertical',
                    left: 'top',
                    data: ['失败总数','成功总数']
                },
                series : [
                    {
                        name: '',
                        type: 'pie',
                        radius : '50%',
                        center: ['50%', '50%'],
                        color: ['#67beea', 'tomato'],
                        data:[
                          {value:allSuccessSum, name:'成功总数'},
                          {value:allFailSum, name:'失败总数'}                     
                        ],
                        itemStyle: {
                            emphasis: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        }
                    }
                ]
            };
              myChart1.setOption(option);
              myChart2.setOption(option2);
          })
        }
        
        initDay(1);
        $(window).on('resize',function(){
          initDay(1);
        })
      </script>
    </body>
  </html>

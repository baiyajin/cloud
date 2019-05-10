{
tooltip : {
trigger: 'axis'
},
grid:{
bottom:42,
left:55,
top:10
},
calculable : true,
legend:{
data:[{name:'环比', icon:'rect'},
{name:'同比',icon:'rect'}],
itemHeight:2,
itemWidth:10,
textStyle:{
color : '#fff',
fontSize:10
},
left:200
},
xAxis : [{
type : 'category',
data : ['2017.01','2018.01','2018.01','2018.01','2018.01','2018.01','2018.01','2018.01'],
axisLine: {
show: false
},
splitLine:{
show:false
},
axisLabel:{
color: '#fff', fontSize:10
},
}],
yAxis : [{
type : 'value',
min: function(value) {
return Math.floor(value.min);
},
minInterval: 0,
splitNumber:4,
axisLine: {
show: false
},
splitLine: {
show: false,
},
axisTick:{
show:false
},
axisLabel:{
show: true,
color: '#55D7FD', fontSize:10
},
}],
series: [{
name:'同比',
symbol: 'none',
symbolSize: 0,
data:[0,10,20,10,5,2,9,2],
itemStyle: {
normal: {
color: "#02e1a2",
lineStyle: {
color: "#02e1a2"
}
}
},
type: 'line'
},{
name:'环比',
type:'line',
symbol: 'none',
symbolSize: 0,
data:[0,22,15,20,16,2,8,0],
itemStyle: {
normal: {
color: "#2b52fa",
lineStyle: {
color: "#2b52fa"
}
}
},
}]
}
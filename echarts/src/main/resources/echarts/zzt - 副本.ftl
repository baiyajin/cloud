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
xAxis : [
{
type : 'category',
data : ['昆明市','曲靖市','昭通市','玉溪市','丽江市','保山市','红河州','昆明市','曲靖市','昭通市','玉溪市','丽江市','保山市','红河州','保山市','红河州'],
axisLine: {
show: false
},
splitLine:{
show:false
},
axisLabel:{
color: '#fff',
fontSize:10,
interval:0
},
}
],
yAxis : [
{
type : 'value',
min: function(value) {
return Math.floor(value.min);
},
minInterval: 0,
splitNumber:4,
axisLine: {
show: false
},
axisLabel: {
show: true
},
splitLine: {
show: false,
},

axisTick:{
show:false
},
axisLabel:{
color: '#55D7FD', fontSize:10
},
}
],
series : [
{
symbol: 'none',
symbolSize: 0,
type:'bar',
itemStyle: {
normal: {
color: new this.$echarts.graphic.LinearGradient(0, 0, 0, 1, [{
offset: 0,
color: '#38effd'
}, {
offset: 1,
color: '#005fe2'
}]),
lineStyle: {
color: "#02e1a2"
}
}
},
data:[15,10,20,10,5,2,9,2,15,10,20,10,5,2,9,2],
barWidth:14,
barGap:'40%'
}]
}
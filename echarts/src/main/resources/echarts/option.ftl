{
title: {
text:'${title}',
x:'middle',
textAlign:'center'
},
xAxis : [
{
data: ${categories},
type : 'category',
data : '',
axisLine: {
show: false
},
splitLine:{
show:false
}
}
],
yAxis: {
type: 'value'
},
series: [{
data: ${values},
type: 'pie'
}]
}
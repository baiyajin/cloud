{
color:['#ffaf25', '#215973'],
series: [{
center: ['25%', '50%'],
name:'昆明',
type:'pie',
radius: ['50%', '60%'],
avoidLabelOverlap: false,
label: {
normal: {
show: true,
position: 'center'
},
emphasis: {
show: true,

textStyle: {
fontSize: '30',
fontWeight: 'bold'
}
},
},
labelLine: {
normal: {
show: false
}
},
data:[{
value:100,
name:'昆明',
itemStyle: {
normal: {
color: '#25ff8c',
shadowColor: 'rgba(37, 255, 140, 1)',
shadowBlur: 20
},
},
label:{
formatter: [
'{a|昆明}',
'{b|12345}'
].join('\n'),

rich: {
a: {
color: '#fff',
fontSize: '14',
lineHeight:'34'
},
b: {
color: '#25ff8c',
fontSize: '14'
},
}
}
},{
value:55, name:'',itemStyle: { color: '#215973' }
}]
},{
center: ['75%', '50%'],
name:'钢材',
type:'pie',
radius: ['50%', '60%'],
avoidLabelOverlap: false,
label: {
normal: {
show: true,
position: 'center'
},
emphasis: {
show: true,
textStyle: {
fontSize: '30',
fontWeight: 'bold'
}
}
},
labelLine: {
normal: {
show: false
}
},
data:[{ value:100,
name:'钢材',
itemStyle: {
normal: {
color: '#ffaf25',
shadowColor: 'rgba(255, 175, 37, 1)',
shadowBlur: 20
},
},
label:{
formatter: [
'{a|钢材}',
'{b|45678}'
].join('\n'),
rich: {
a: {
color: '#fff',
fontSize: '14',
lineHeight:'34'
},
b: {
color: '#25ff8c',
fontSize: '14'
},
}
}
},{
value:20, name:'',itemStyle: { color: '#215973' }
}]
}]
}
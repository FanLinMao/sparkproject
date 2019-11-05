var chart;
new Vue({
    el: '#spark_rdd',
    data: function () {
        return {
            index: "3",
            activeIndex: "1",
            activeIndex2: "3",
            gender: "",
            genders: [
                {label:"男",value: "M"},
                {label:"女",value: "F"}
            ],
            ageGroup: "",
            ageGroups: [
                {label: "18岁以下",value:"1"},
                {label: "18-24",value:"18"},
                {label: "25-34",value:"25"},
                {label: "35-44",value:"35"},
                {label: "45-49",value:"45"},
                {label: "50-55",value:"50"},
                {label:"56+",value:"56"}
            ]
        }
    },
    mounted(){
        chart = echarts.init(document.getElementById("main"));
    },
    methods: {
        handleSelect(key, keyPath) {
            console.log(key, keyPath)
        },
        handleSelect2(key, keyPath) {
            console.log(key)
            if (key === this.index) {
                return
            } else if (key === "1") {
                window.location.href = 'spark_rdd.html';
            } else if (key === "2") {
                window.location.href = 'spark_streaming.html';
            }
        },
        search_rating() {
            let parm = new URLSearchParams()
            parm.append("gender", this.gender)
            parm.append("age", this.ageGroup)
            axios.post("http://localhost:8080/getAllTypeAverageNum", parm).then(function (response) {
                if (response.data.status === '200') {
                    let xdate = [];
                    let ydate = [];
                    for (let key in response.data.data) {
                        xdate.push(key);
                        ydate.push(response.data.data[key])
                    }
                    chart.setOption({
                        xAxis: [{
                            data:xdate
                        }],
                        series:[{
                            data:ydate
                        }]
                    })
                }
            })
        }
    }
});


option = {
    color: ['#3398DB'],
    tooltip: {
        trigger: 'axis',
        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        }
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    xAxis: [
        {
            type: 'category',
            data: ["Action","Adventure", "Animation", "Children","Comedy", "Crime","Documentary","Drama","Fantasy","Film-Noir","Horror","Musical","Mystery","Romance","Sci-Fi","Thriller", "War", "Western"],
            axisTick: {
                alignWithLabel: true
            }
        }
    ],
    yAxis: [
        {
            type: 'value'
        }
    ],
    series: [
        {
            name: '评分',
            type: 'bar',
            barWidth: '60%',
            data: [2,3,1,4,5,6,1,2,3,4,5,2,4,5,3,5,4,2]
        }
    ]
};
chart.setOption(option);
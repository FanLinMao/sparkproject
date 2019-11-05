var myChart;
var socket;

xdate = ['', '', '', '', '', '', '', '', '', ''];
ydate = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0];

var type = "Action";


new Vue({
    el: '#spark_rdd',
    data: function () {
        return {
            index: "2",
            activeIndex: "1",
            activeIndex2: "2",
            movie_type: "",
            movie_types: [
                {value: "Action",},
                {value: "Adventure"},
                {value: "Animation"},
                {value:"Children's"},
                {value:"Comedy"},
                {value:"Crime"},
                {value:"Documentary"},
                {value:"Drama"},
                {value:"Fantasy"},
                {value:"Film-Noir"},
                {value:"Horror"},
                {value:"Musical"},
                {value:"Mystery"},
                {value:"Romance"},
                {value:"Sci-Fi"},
                {value:"Thriller"},
                {value:"War"},
                {value:"Western"}
            ],
        }
    },
    mounted(){
        myChart =  echarts.init(document.getElementById("main"));
        Message();
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
            } else if (key === "3") {
                window.location.href = 'spark_sql.html';
            }
        },
        selectChange(){
            type = this.movie_type
            if (type != null){
                myChart.setOption({
                    title:{
                        text: type + "类型"
                    }
                })
            }
        },
        fileSuccess(response,file,fileList){
            if (response.status === "200"){
                this.$notify({
                    title:"提示",
                    message:"h('i', { style: 'color: teal'}, '文件上传成功')"
                })
            }else {
                this.$notify({
                    title:"提示",
                    message:"h('i', { style: 'color: teal'}, '文件上传失败')"
                })
            }
        },
        fileChange(file,fileList){
            var _this = this;
            if (file.raw) {
                let reader = new FileReader()
                reader.onload = function (e) {
                    _this.contentHtml = e.target.result;
                };
                reader.readAsText(file.raw,'utf-8');

            }
            console.log(file, fileList);
        }
    }
});



option = {
    title:{
        text:"Action类型"
    },
    tooltip:{
        trigger:'axis'
    },
    xAxis: {
        type: 'category',
        data: xdate
    },
    yAxis: {
        type: 'value'
    },
    series: [{
        data: ydate,
        type: 'line'
    }]
};

myChart.setOption(option);

function Message() {
    socket = new WebSocket("ws://localhost:8080/websocket/getStreamingAnalysis");
    socket.onmessage = function (event) {
        let data = JSON.parse(event.data);

        let date = new Date();
        let str = date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
        let num = parseFloat(data);
        xdate.shift()
        xdate.push(str)
        ydate.shift()
        ydate.push(num)

        myChart.setOption({
            xAxis: {
                data: xdate
            },
            series: [{
                data: ydate
            }]
        });
    }
}


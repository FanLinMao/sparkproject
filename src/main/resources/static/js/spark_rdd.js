var rdd = new Vue({
    el: '#spark_rdd',
    data: function () {
        return {
            index: "1",
            currentPage:1,
            activeIndex: "1",
            activeIndex2: "1",
            movie_name: "",
            movie_type: "",
            movie_types: [
                {value: "Action",},
                {value: "Adventure"},
                {value: "Animation"},
                {value:"Children"},
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
            movies:[
                {
                    movieId:"1",
                    title:"oy Story (1995)",
                    geners:"Adventure|Animation|Children|Comedy|Fantasy"
                },
                {
                    movieId:"2",
                    title:"Jumanji (1995)",
                    geners:"Adventure|Children|Fantasy"
                },
                {
                    movieId:"3",
                    title:"Grumpier Old Men (1995)",
                    geners:"Comedy|Romance"
                },
                {
                    movieId:"4",
                    title:"Waiting to Exhale (1995)",
                    geners:"Comedy|Drama|Romance"
                },
                {
                    movieId:"5",
                    title:"Father of the Bride Part II (1995)",
                    geners:"Comedy"
                }
            ],
            total:0,
            currentList:[],

        }
    },
    mounted(){

    },
    methods: {
        handleSelect(key, keyPath) {
            console.log(key, keyPath)
        },
        handleSelect2(key, keyPath) {
            console.log(key)
            if (key === this.index) {
                return
            }
            if (key === "2") {
                window.location.href = 'spark_streaming.html';
            } else if (key === "3") {
                window.location.href = 'spark_sql.html';
            }
        },
        search(){
            let a = this;
            let parm = new URLSearchParams()
            parm.append("movieName",this.movie_name)
            parm.append("movieType",this.movie_type)
            axios.post("http://localhost:8080/searchMovies",parm).then(function (response) {
                if (response.data.status === "200"){
                    a.movies = response.data.data
                    a.currentList = a.movies.splice(0,20)
                    a.total = a.movies.length
                }else {
                    console.log("错误信息",response.data.message)
                    console.log(response.data.status)
                }
            })
            console.log("search")
        },
        handleCurrentChange(val) {
            let pre = 0 + (val - 1) * 20
            let next = 20 + (val - 1) * 20
            this.currentList = this.movies.slice(pre, next)
        }
    }
});
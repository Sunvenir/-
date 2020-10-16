<template>
    <div :class="post">
       <el-button v-show="post1" @click="choosepost(1)" type="danger" style="float: left;height:30px;line-height: 0px;margin-left: 0px;margin-right:15px">发帖
        </el-button>
        <el-button v-show="post1&&isteacher" @click="choosepost(2)" type="danger" style="float: left;height:30px;line-height: 0px;margin-left: 0px;margin-right:15px">更新规则
        </el-button>
        <input v-show="post2" class="title" type="text" maxlength="30" placeholder="请输入标题" v-model="title">
        </input>
        <span v-show="post2" style="color:orange;position: absolute;right: 20px;top: 21px;">
            {{descNum}}/30
        </span>
        <div v-show="post2" class="input" >
            <div class="textarea" contenteditable="true" v-html="input" @blur="input=$event.target.innerText">
            </div>
        </div>
        <div v-show="post2" class="type">
            <el-button @click="postinv()" type="danger" style="float: right;height:30px;line-height: 0px;margin-left: 0px;margin-right:15px">发帖
            </el-button>
            <el-button @click="choosepost(0)" type="danger" style="float: right;height:30px;line-height: 0px;margin-left: 0px;margin-right:15px">收起
            </el-button>
        </div>
        <input v-show="post3" class="title" type="text" maxlength="30" placeholder="请输入圈子新名称" v-model="title">
        </input>
        <div v-show="post3" class="input" >
            <div class="textarea" contenteditable="true" v-html="input" @blur="input=$event.target.innerText">
            </div>
        </div>
        <div v-show="post3" class="type">
            <el-button @click="postnotice()" type="danger" style="float: right;height:30px;line-height: 0px;margin-left: 0px;margin-right:15px">更新规则
            </el-button>
            <el-button @click="choosepost(0)" type="danger" style="float: right;height:30px;line-height: 0px;margin-left: 0px;margin-right:15px">收起
            </el-button>
        </div>
    </div>
</template>

<script type="text/javascript" defer="true">
    import Display from "./Display.vue";
    export default {
        name:"Tags",
        components:{
            Display
         },
        props:{
            notice:String,
            isteacher:Boolean,
            communityid:String
        },
        data() {
            return {
                input: '',
                title:'',
                type:'',
                uid:0,
                post:'posta',
                post1:true,
                post2:false,
                post3:false,
            }
        },
        mounted(){
            this.uid=window.sessionStorage.getItem("uid");
            //alert(this.uid);
        },
        methods:{
            postnotice(){
                this.$axios.post("/api/modifyCommunity",{
                    id:this.communityid,
                    name:this.title,
                    rules:this.input,
                })
                .then(res => {
                    if(res.data.code === 200){
                        this.$emit("postnotice");
                    }
                    else{
                        this.$message.error(res.data.message);
                    }
                })
                .catch(failResponse=>{
                    console.log(failResponse);
                    this.$message.error('网络错误');
                });
                
            },
            choosepost(i){
                if(i==0){
                    this.post="posta"
                    this.post1=true
                    this.post2=false
                    this.post3=false
                }
                else if(i==1){
                    this.post="postb"
                    this.post1=false
                    this.post2=true
                    this.post3=false
                    this.title=''
                    this.input=''
                }
                else if(i==2){
                    this.post="postc"
                    this.post1=false
                    this.post2=false
                    this.post3=true
                    this.input=this.notice
                }
            },
            getNowFormatDate() {
                var date = new Date();
                var seperator1 = "-";
                var seperator2 = ":";
                var month = date.getMonth() + 1;
                var strDate = date.getDate();
                if (month >= 1 && month <= 9) {
                    month = "0" + month;
                }
                if (strDate >= 0 && strDate <= 9) {
                    strDate = "0" + strDate;
                }
                var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
                        + " " + date.getHours() + seperator2 + date.getMinutes()
                        + seperator2 + date.getSeconds();
                return currentdate;
            },
            postinv(){
                if(this.title==""){
                    alert("请输入标题");
                    return;
                }
                 if(this.input==""){
                    alert("请输入内容");
                    return;
                }
               /* console.log(this.title+" "+this.input+" "+this.uid+" "+window.sessionStorage.getItem("class"))*/
               
                this.$axios.post("/api/post",{
                    head:this.title,
                    text:this.input,
                    time:this.getNowFormatDate(),
                    poster:this.uid,
                    courseid:window.sessionStorage.getItem("class"),
                })
                .then(res => {
                    if(res.data.code === 200){
                        this.$emit("postinv");
                        this.title='';
                        this.input='';
                    }
                    else{
                        this.$message.error(res.data.message);
                    }
                })
                .catch(failResponse=>{
                    console.log(failResponse);
                    this.$message.error('网络错误');
                });
            },
        },
        computed: {
            descNum() {
              if(this.title.length>=30){
                this.title=this.title.substr(0,30);
                return 30;
              }
              return this.title.length == 0?0:this.title.length;
            },
        },
    };
</script>

<style scoped>
    .input_control{
        margin:0px auto;
    }
    input[type="text"],#btn1,#btn2{
        box-sizing: border-box;
        border:0px;
        color:#6a6f77;
        -web-kit-appearance:none;
        -moz-appearance: none;
        display:block;
        text-decoration:none;
        width:100%;
        background: #eeeeee;
    }
    .posta{
        margin: 0 auto;
        margin-top: 10px;
        width: 770px;
        height:30px;
        padding:15px;
        background: #eeeeee;
    }
    .postb{
        width: 770px;
        margin: 0 auto;
        margin-top: 10px;
        background: #eeeeee;
        padding:15px;
        position: relative;
        overflow: hidden;
    }
     .postc{
        width: 770px;
        margin: 0 auto;
        margin-top: 10px;
        background: #eeeeee;
        padding:15px;
        position: relative;
        overflow: hidden;
    }
    .title{
        width: 760px;
        height:30px;
        overflow:hidden;
        line-height: 30px;
        text-align: left;
        margin-bottom: 15px;
        outline: 2px solid grey;
        padding-left:5px;
        padding-right: 5px;
        font-size: 20px;
        -webkit-animation: move1 0.4s;
    }
    @keyframes move1
    {
        0% {
            margin-top: -160px;
        }
        100% {
            margin-top: 0px;
        }
    }
    .title:focus{
        outline: 2px solid red;
    }
    .input{
        outline: 2px solid grey;
        width:760px;
        padding:5px;
        min-height: 90px;
        max-height: 310px;
    }
    .input:focus-within{
       outline: 2px solid red;
    }
    .textarea{
        outline: none;
        min-height: 80px;
        max-height: 280px;
        overflow-y: auto;
        text-align: left;
        font-size: 15px;
    }
    .type{
        height:30px;
        width: 100%;
        margin-top: 15px;
        font-size: 15px;
        line-height: 30px
    }
</style>
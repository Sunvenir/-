<template>
    <div class="inv">
        <div class="head">
            {{istop}}【{{types}}】{{title}}
        </div>
        <div class="text" :style="newstyle()" @click="lengthen()">
            {{text}}
        </div>
        <div class="operation" style="margin-bottom:20px;">
            <div style="overflow: hidden;float: left;height:30px;width: 30px;border-radius: 30px">
                <el-image :src="avatar" ></el-image>
            </div>
            <div style="height:30px;width:100px;overflow: hidden;font-size: 15px;line-height: 30px;float: left;">
                {{author}}
            </div>
            <div style="height:30px;width:fit-content;font-size: 15px;line-height: 30px;float: left">
                {{time}}
            </div>
            <el-button @click="like()" type="danger" style="float: right;height:30px;line-height: 0px;margin-left: 0px">点赞{{follow}}
            </el-button>
            
            
            <el-button @click="mycomment()" type="danger" style="float: right;height:30px;margin-right:15px;line-height: 0px;margin-left: 0px">评论{{comnum}}
            </el-button>
            <el-button v-show="isteacher" @click="setop()" type="danger" style="float: right;height:30px;margin-right:15px;line-height: 0px;margin-left: 0px">{{topop}}

            </el-button>
             <el-button v-show="isteacher||uid==puid" @click="deletepost()" type="danger" style="float: right;height:30px;margin-right:15px;line-height: 0px;margin-left: 0px">删除
            </el-button>
            
            <el-button v-show="this.type==1?(false&&isteacher):(true&&isteacher)" @click="setgood()" type="danger" style="float: right;height:30px;margin-right:15px;line-height: 0px;margin-left: 0px">+精华
            </el-button>
        </div>
        <div style="background-color:#eeeeee;">
        <div style="height:30px;width:100%;border-top:black solid 1px;" v-show="commentview"></div>
        <div class="input" v-show="commentview" style="width:700px;margin:0 auto">
            <div class="textarea" maxlength="10" contenteditable="true" v-html="input1" @blur="input1=$event.target.innerText" style="width:100%;">
            </div>
        </div>
        <div class="operation" v-show="commentview" style="background-color:#eeeeee;width:710px;margin:0 auto;margin-top:15px">
            <el-button type="danger" @click="postcomment()" style="float: right;height:30px;line-height: 0px;margin-left: 0px;margin-right:0px">发布
            </el-button>
            <el-button type="danger" @click="clear()" style="float: right;height:30px;line-height: 0px;margin-left: 0px;margin-right:15px">清空
            </el-button>
        </div >
        <div class="line" v-show="commentview"></div>
        <div v-show="commentview" class="menu" style="background-color:#eeeeee;">
            <div class="tag1" :style="newstyle1(2)" @click="choose2()">
              按点赞
            </div> 
            <div class="tag1" :style="newstyle1(1)" @click="choose1()">
              按时间
            </div>
          </div>
        <div v-if="commentview" style="width:770px;margin: 0 auto;box-shadow:0 0 3px #666666;" v-for="item in comments">
            <Comment
                :authorid="item.commentuserid"
                :author="item.commentusername"
                :time="item.comtime"
                :avatar="item.commentuseravatar"
                :mycomment="item.content"
                :comid="item.comid"
                :follow="item.likes"
                :authorrealid="item.userid"
                v-on:freshcom="newComments()"

            >
            </Comment>
        </div>
        </div>
    </div>
</template>

<script>
    import Comment from "./Comment.vue";
    export default {
        components:{
           Comment,
        },
        props:{
            top:Number,
            type:Number,
            author:String,
            pid:Number,
            avatar:String,
            title:String,
            text:String,
            time:String,
            ppid:Number,
            follow:Number,
            comnum:Number,
            isteacher:Boolean,
            puid:String,
        },
        data() {
            return {
                length:"",
                op:"展开",
                input:'',
                input1:'',
                commentview:false,
                comments:[],
                uid:window.sessionStorage.getItem("uid"),
                menuNo:1,
            }
        },
        mounted(){
            this.uid=window.sessionStorage.getItem("uid");
            console.log(this.puid+" "+this.uid)
            this.newComments();
        },
        methods:{
             deletepost(){
                this.$axios.post("/api/deletepost",{
                    postid:this.ppid,
                })
                .then(res => {
                if(res.data.code === 200){
                   this.$emit("freshinv");
                }
                else{
                  this.$message.error(res.data.message);
                }
                })
                .catch(failResponse=>{
                  console.log(failResponse);
                  this.$message.error('网络错误');
                })
            },
            choose1(){
                this.menuNo=1;
                this.newComments();
            },
            choose2(){
                this.menuNo=2;
                this.newComments();
            },
            newstyle1(i){
                if(i==this.menuNo)
                    return "background:black;color:white;box-shadow:0 0 10px #666666";
                else
                    return "background:#cccccc;color:black;box-shadow:0 0 0px #666666";
            },
            like(){
                this.$axios.post("/api/likepost",{
                    postid:this.ppid,
                })
                .then(res => {
                if(res.data.code === 200){
                   this.$emit("freshinv");
                }
                else{
                  this.$message.error(res.data.message);
                }
                })
                .catch(failResponse=>{
                  console.log(failResponse);
                  this.$message.error('网络错误');
                })
            },
            setgood(){
                this.$axios.post("/api/setdigest",{
                        postid:this.ppid,
                    })
                .then(res => {
                if(res.data.code === 200){
                   this.$emit("freshinv");
                }
                else{
                  this.$message.error(res.data.message);
                }
                })
                .catch(failResponse=>{
                  console.log(failResponse);
                  this.$message.error('网络错误');
                })
            },
            setop(){
                if(this.top==0){
                    this.$axios.post("/api/toppost",{
                        postid:this.ppid,
                    })
                    .then(res => {
                    if(res.data.code === 200){
                      
                       this.$emit("freshinv");
                    }
                    else{
                      this.$message.error(res.data.message);
                    }
                    })
                    .catch(failResponse=>{
                      console.log(failResponse);
                      this.$message.error('网络错误');
                    })
                }
                else{
                     this.$axios.post("/api/canceltop",{
                        postid:this.ppid,
                    })
                    .then(res => {
                    if(res.data.code === 200){
                     
                       this.$emit("freshinv");
                    }
                    else{
                      this.$message.error(res.data.message);
                    }
                    })
                    .catch(failResponse=>{
                      console.log(failResponse);
                      this.$message.error('网络错误');
                    })
                }
            },
            newComments(){
                this.$axios.post("/api/showcomment",{
                    postid:this.ppid,
                    sort_style:this.menuNo,
                })
                .then(res => {
                if(res.data.code === 200){
                    console.log(res.data.detail);
                    this.comments=res.data.detail;
                    for(var i=0;i<this.comments.length;i++){
                        if(this.comments[i].comtime.length>2)
                        this.comments[i].comtime=this.comments[i].comtime.substring(0,this.comments[i].comtime.length-2);
                    }
                    this.$emit("freshinv");
                }
                else{
                  this.$message.error(res.data.message);
                }
                })
                .catch(failResponse=>{
                  console.log(failResponse);
                  this.$message.error('网络错误');
                })
            },
            lengthen(){
                if(this.op=="展开"){
                    this.op="收起";
                    this.length="max-height: none;";
                }
                else{
                    this.op="展开";
                    this.length="max-height: 80px;";
                }
            },
            newstyle(){
                return this.length;
            },
            mycomment(){
                if(this.commentview!=true)
                    this.commentview=true;
                else
                    this.commentview=false;
                this.newComments();
            },
            clear(){
                this.input1='';
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
            postcomment(){
                if(this.input1==''){
                    alert("请输入评论");
                    return;
                }
                this.$axios.post("/api/comment",{
                    comtime:this.getNowFormatDate(),
                    commentuser:this.uid,
                    commonPost:this.ppid,
                    content:this.input1,
                })
                .then(res => {
                    if(res.data.code === 200){
                      
                        this.input1='';
                        this.newComments();
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
        computed:{
            topop(){
                if(this.top==1)
                    return "取消置顶";
                else
                    return "置顶";
            },
            istop(){
                if(this.top==1)
                    return "⇧置顶";
                else
                    return "";
            },
            types(){
                if(this.type==1)
                    return "精华帖";
                else
                    return "水贴";
            },
        },
    };
</script>

<style scoped>
    .inv{
        background-image: url("/picture/3.jpg");
        background-repeat: no-repeat;
        background-size: cover;
        background-position: inherit;
        width: 770px;
        margin: 0 auto;
        margin-top: 10px;
        background-color: white;
        padding:15px;
    }
    .line{
        margin-top: 20px;
        height: 30px;
        width: 100%;
        background: url('../assets/icon/line.png') no-repeat;
        background-size: contain;
    }
    .head{
        width: 100%;
        height:30px;
        line-height: 30px;
        text-align: left;
        font-size: 20px;
        border-bottom: 1px #666666 solid;
    }
    .text{
        width: 760px;
        padding:5px;
        max-height: 80px;
        background:white;
        line-height: 30px;
        text-align: left;
        font-size: 15px;
        overflow:hidden;
        word-wrap:break-word;
    }
    .operation{
        height:30px;
        width: 100%;
        margin-top: 15px;
        font-size: 15px;
        line-height: 30px;
        background-color: white;
    }
    .input{
        outline: 2px solid grey;
        width:760px;
        padding:5px;
        min-height: 20px;
        max-height: 310px;
        background: white;
        margin-top: 15px;
    }
    .input:focus-within{
       outline: 2px solid red;
    }
    .textarea{
        outline: none;
        min-height: 15px;
        max-height: 280px;
        overflow-y: auto;
        text-align: left;
        font-size: 15px;
    }
    .menu{
    margin: 0 auto;
    margin-top: 10px;
    width: 710px;
    height:30px;
    background: white;
  }
  .tag1{
    width: 80px;
    background: pink;
    height:30px;
    float: right;
    line-height: 30px;
    font-size: 15px;
    cursor: pointer;
    margin-left: 5px;

  }
</style>
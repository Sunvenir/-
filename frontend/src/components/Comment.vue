<template>
    <div class="comment">
        <div @click="lengthen()" class="text" :style="newstyle()" >
            {{mycomment}}
        </div>
        <div class="operation">
            <div style="overflow: hidden;float: left;height:30px;width: 30px;border-radius: 30px">
                <el-image :src="avatar" ></el-image>
            </div>
            <div style="height:30px;width:fit-content;font-size: 15px;line-height: 30px;float: left;margin-left: 20px">
                {{author}}{{time}}
            </div>
            <el-button @click="like()" type="danger" style="float: right;height:30px;line-height: 0px;margin-left: 0px">点赞{{follow}}
            </el-button>
            <el-button v-show="authorrealid==uid" @click="dedetecomment()" type="danger" style="float: right;height:30px;margin-right:15px;line-height: 0px;margin-left: 0px">删除
            </el-button>
        </div>
        <div style="height:5px;width:100%;"></div>
    </div>
</template>

<script>
    export default {
        name:"Tags",
        components:{
           
         },
        props:{
            author:String,
            authorid:Number,
            authorrealid:String,
            avatar:String,
            mycomment:String,
            time:String,
            comid:Number,
            follow:Number,
        },
        data() {
            return {
                length:"",
                op:"展开",
                uid:window.sessionStorage.getItem("uid"),
            }
        },
        mounted(){
            this.uid=window.sessionStorage.getItem("uid");
        },
        methods:{
             like(){
                this.$axios.post("/api/likecomment",{
                    comid:this.comid,
                })
                .then(res => {
                if(res.data.code === 200){
                  
                   this.$emit("freshcom");
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
            dedetecomment(){
                this.$axios.post("/api/deleteComment",{
                    commentid:this.comid,
                })
                .then(res => {
                if(res.data.code === 200){
                   this.$emit("freshcom");
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
                    this.length="max-height: none;-webkit-line-clamp:unset;";
                }
                else{
                    this.op="展开";
                    this.length="max-height: 80px;-webkit-line-clamp: 3;";
                }
            },
            newstyle(){
                return this.length;
            },
        }
    };
</script>

<style scoped>
    .text{
        width: 700px;
        margin: 0 auto;
        margin-top: 15px;
        padding:5px;
        max-height: 80px;
        line-height: 30px;
        text-align: left;
        font-size: 15px;
        overflow:hidden;
        word-wrap:break-word;
        display: -webkit-box;
        -webkit-line-clamp: 3;
        -webkit-box-orient: vertical;
        overflow: hidden;
    }
    .operation{
        height:30px;
        width: 710px;
        margin: 0 auto;
        margin-top: 15px;
        font-size: 15px;
        line-height: 30px
    }
    .commet{
        background-color: white;
        box-shadow: 0 0 10px #666666;
    }
</style>
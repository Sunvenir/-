<template>
    <div class="tag" :style="myStyle()" @click="focus()" @mouseover="mouseOver()" @mouseout="mouseOut()" >
        {{title}}
    </div>
</template>

<script>
    export default {
        name:"Tags",
        props:{
            id:Number,
            title:String,
            selected:Number
        },
        data() {
            return {
                dst:"",
            };
        },
        methods:{
            focus(){
                this.$emit("select",this.id);
            },
            myStyle(){
                //alert(this.id+" "+this.selected)
                if(this.id==this.selected)
                    return "color: red;"
                else
                    return "";
            },
            mouseOver(){
                this.$emit("over",this.id);
            },
            mouseOut(){
                this.$emit("out",this.id);
            },
        }
    }
</script>

<style scoped>
    .tag{
        width:100px;
        height:50px;
        background: white;
        float: left;
        border-bottom: 2px solid grey;
        color: black;
        font-size: 14px;
        line-height: 50px;
        cursor: pointer;
    }
    .tag:hover{
        color: red;
    }
</style>
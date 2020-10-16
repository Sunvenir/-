<template>
    <div class="tabs">
        <div v-for="item in tags" :key="item.id" >
          <Tags
            :id="item.id"
            :title="item.title"
            :selected="selected" v-on:select="change($event)"
            v-on:over="mouseover($event)"
            v-on:out="mouseout($event)">
            </Tags>
        </div>
        <div id="mov">
        </div>
    </div>
</template>

<script>
    import Tags from "./Tags.vue";
    export default {
        name:"Tabs",
        components: {
            Tags
        },
        data() {
            return {
                tag:{
                    id: 0,
                    title:"数学分析",
                },
                tags:[],
                selected: 0,
                oldover:0,
                over:0,
                out:0,
            };
        },
        mounted(){
            for(var i=0;i<=7;i++){
                var tag1={
                    id:0,
                    title:""
                };
                this.tag.title="数学分析"+i;
                tag1.id=this.tag.id;
                tag1.title=this.tag.title;
                this.tags.push(tag1);
                this.tag.id++;
            }
            /*for(var i=0;i<=7;i++){
                alert(this.tags[i].id);
            }*/
        },
        methods:{
            change(i){
                this.selected=i;
                this.$emit("changeCourse",this.selected);
            },
            automove(i){
                var tween = {
                    linear:function(t,b,c,d){
                        return c*t/d + b;
                    },
                    easeIn:function(t,b,c,d){
                        return c * ( t /= d ) * t + b;
                    },
                    strongEaseIn:function(t,b,c,d){
                        return c * ( t /= d ) * t * t * t * t + b;
                    },
                    strongEaseOut:function(t,b,c,d){
                        return c * ( ( t = t / d -1 ) * t * t * t * t +1 ) + b;
                    },
                    sineaseIn:function(t,b,c,d){
                        return c * ( t /= d ) * t * t + b;
                    },
                    sineaseOut:function(t,b,c,d){
                        return c * ( ( t = t / d -1 ) * t * t *t +1 ) + b;
                    }
                };

                var Animate = function(dom){
                    this.dom = dom;
                    this.startTime = 0;
                    this.startPos = 0;
                    this.endPos = 0;
                    this.propertyName = null;
                    this.easing = null;
                    this.duration = null;
                }

                Animate.prototype.start = function(propertyName,startPos,endPos,duration,easing){
                    this.startTime = +new Date;
                    this.startPos = this.dom.getBoundingClientRect()[propertyName];
                    this.propertyName = propertyName;
                    this.endPos = endPos;
                    this.duration = duration;
                    this.easing = tween[easing];
                    this.startPos=startPos;
                    //alert(this.startPos+" "+this.endPos);
                    var self = this;
                    var timeId = setInterval(function(){
                        if(self.step() === false){
                            clearInterval(timeId);
                        }
                    },19);
                }

                Animate.prototype.step = function(){
                    var t = +new Date;
                    if(t>=this.startTime + this.duration){
                        this.update(this.endPos);
                        return false;
                    }
                    var pos = this.easing(t-this.startTime, this.startPos, this.endPos - this.startPos, this.duration);
                    this.update(pos);
                }

                Animate.prototype.update = function(pos){
                    this.dom.style[this.propertyName] = pos + 'px';
                }
                var div = document.getElementById('mov');
                var animate = new Animate(div);
                animate.start('left',this.oldover*100,this.over*100,150,'linear');
            },
            automoveBack(){
                var tween = {
                    linear:function(t,b,c,d){
                        return c*t/d + b;
                    },
                    easeIn:function(t,b,c,d){
                        return c * ( t /= d ) * t + b;
                    },
                    strongEaseIn:function(t,b,c,d){
                        return c * ( t /= d ) * t * t * t * t + b;
                    },
                    strongEaseOut:function(t,b,c,d){
                        return c * ( ( t = t / d -1 ) * t * t * t * t +1 ) + b;
                    },
                    sineaseIn:function(t,b,c,d){
                        return c * ( t /= d ) * t * t + b;
                    },
                    sineaseOut:function(t,b,c,d){
                        return c * ( ( t = t / d -1 ) * t * t *t +1 ) + b;
                    }
                };

                var Animate = function(dom){
                    this.dom = dom;
                    this.startTime = 0;
                    this.startPos = 0;
                    this.endPos = 0;
                    this.propertyName = null;
                    this.easing = null;
                    this.duration = null;
                }

                Animate.prototype.start = function(propertyName,startPos,endPos,duration,easing){
                    this.startTime = +new Date;
                    this.startPos = this.dom.getBoundingClientRect()[propertyName];
                    this.propertyName = propertyName;
                    this.endPos = endPos;
                    this.duration = duration;
                    this.easing = tween[easing];
                    this.startPos=startPos;
                    //alert(this.startPos+" "+this.endPos);
                    var self = this;
                    var timeId = setInterval(function(){
                        if(self.step() === false){
                            clearInterval(timeId);
                        }
                    },19);
                }

                Animate.prototype.step = function(){
                    var t = +new Date;
                    if(t>=this.startTime + this.duration){
                        this.update(this.endPos);
                        return false;
                    }
                    var pos = this.easing(t-this.startTime, this.startPos, this.endPos - this.startPos, this.duration);
                    this.update(pos);
                }

                Animate.prototype.update = function(pos){
                    this.dom.style[this.propertyName] = pos + 'px';
                }
                var div = document.getElementById('mov');
                var animate = new Animate(div);
                animate.start('left',this.oldover*100,this.selected*100,150,'linear');
            },
            mouseover(i){
                this.oldover=this.over;
                this.over=i;
                this.automove();
                this.out=1;
            },
            mouseout(i){
                this.out=0;
                this.automoveBack();
            },
        }

    }
</script>

<style scoped>
    .tabs{
        width: 800px;
        height:50px;
        background: transparent;
        margin:0 auto;
        margin-top: 10px;
        margin-bottom: 10px;
        border-bottom: 2px solid grey;
        position: relative;
    }
    #mov{
        height: 4px;
        background-color: red;
        width: 60px;
        position: absolute;
        margin-top:48px;
        margin-left: 20px;
    }
</style>
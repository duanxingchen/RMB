# 该镜像需要依赖的基础镜像
FROM node:12.22
# 指定维护者名称
WORKDIR /app
COPY . /app
#RUN npm install
# 声明服务运行在8080端口
EXPOSE 9090
# 执行命令
CMD npm run dev

#docker build -t containerName（自定义）:tag（自定义）.
#注意最后的 . 一定要有 #完成构建后可以使用命令dokcer images查看build完的镜像

#docker build -t web:1.0.0 .

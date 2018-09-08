# build image

FROM naartjie/alpine-lein as builder

# set working directory
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

# add app
COPY . /usr/src/app

# generate build
RUN lein build-release

# production image

FROM nginx:1.15.3-alpine

# copy artifact build from the 'build environment'
COPY --from=builder /usr/src/app/resources/public/ /usr/share/nginx/html

EXPOSE 80

# Run nginx
CMD ["nginx", "-g", "daemon off;"]

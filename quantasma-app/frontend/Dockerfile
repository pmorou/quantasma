FROM nginx:alpine

ARG BACKEND_SERVICE_HOST
ARG BACKEND_SERVICE_PORT
ARG BACKEND_ADDRESS=${BACKEND_SERVICE_HOST}:${BACKEND_SERVICE_PORT}

COPY nginx.conf /etc/nginx/nginx.conf
RUN sed -i "s/replecable_backend_address/${BACKEND_ADDRESS}/g" /etc/nginx/nginx.conf

WORKDIR /usr/share/nginx/html

COPY dist/ .
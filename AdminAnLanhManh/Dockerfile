# Purpose - Leverage Docker multi-stage layers to cache node_modules and achieve faster build times

# Stage 1 - Create cached node_modules.
# Only rebuild layer if package.json has changed

FROM node:10.15-alpine as node_cache

WORKDIR /cache/

COPY package*.json ./

RUN npm install

#Stage 2 - Build and start app
FROM node:12.18.2

# set working directory
WORKDIR /app

RUN npm install -g @angular/cli

COPY --from=node_cache /cache/ .

# Copy source files, and possibily invalidate so we have to rebuild
COPY . .

# default BUILD_ENV
ENV BUILD_ENV ''

# start app
CMD ng serve --host 0.0.0.0 -c ${BUILD_ENV}

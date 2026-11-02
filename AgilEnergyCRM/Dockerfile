# Étape 1 : Build
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
# ON FORCE LE BASE-HREF À /
RUN npm run build -- --configuration=production --base-href /

# Étape 2 : Runtime
FROM nginx:alpine
RUN rm -rf /usr/share/nginx/html/*
# outputPath d'angular.json : dist/stage-pfe (le builder ajoute /browser)
COPY --from=build /app/dist/stage-pfe/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
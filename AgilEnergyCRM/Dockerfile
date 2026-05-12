# Étape 1 : Build
FROM node:18-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
# ON FORCE LE BASE-HREF À /
RUN npm run build -- --configuration=production --base-href /

# Étape 2 : Runtime
FROM nginx:alpine
RUN rm -rf /usr/share/nginx/html/*
# On copie bien le contenu du dossier browser (vérifie si c'est stage-pfe ou AgilEnergyCRM dans dist)
COPY --from=build /app/dist/stage-pfe/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
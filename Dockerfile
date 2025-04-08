FROM openjdk:21-slim

WORKDIR /app

RUN apt-get update && apt-get install -y \
    x11-apps \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libfreetype6 \
    libfontconfig1 \
    libx11-6 \
    libxrandr2 \
    libgtk-3-0

COPY . /app

CMD ["bash"]

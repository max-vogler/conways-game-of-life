if (!Detector.webgl) Detector.addGetWebGLMessage();

var cells = null;
var socket;
var scene = new THREE.Scene();
var camera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, 1, 10000);
var renderer = new THREE.WebGLRenderer();
var controls, particles;

var size;

new Vue({
    el: 'body',
    data: {
        mode : "view",
        play : true,
    },

    watch : {
        mode : function (mode) {
            controls.setMode(mode);
        }
    },

    ready : function () {
        var game = this;

        new Loader({vertex: 'scripts/vertexshader.c', fragment: 'scripts/fragmentshader.c'}).onComplete = function (sources) {
            game.shaders = sources;

            $.get("/api/v1/size").done(function (newSize) {
                size = newSize;
                game.init();
            });

            socket = new GameSocket();
            socket.onGameUpdated = function display(buffer) {
                cells = new Float32Array(size * size);
                for (var i = 0; i < buffer.data.length; i++) {
                    cells[i] = buffer.data.charAt(i) == ' ' ? 0.0 : 1.0;
                }
            };
        };
    },

    methods : {
        init : function () {
            var width = size * 2;
            var height = width;

            camera.position.z = width;

            var vertices = (new THREE.PlaneGeometry(width, height, size - 1, size - 1)).vertices;
            var positions = new Float32Array(vertices.length * 3);
            var colors = new Float32Array(vertices.length * 3);
            var states = new Float32Array(vertices.length);
            var color = new THREE.Color();

            for (var i = 0, l = vertices.length; i < l; i++) {
                var vertex = vertices[i];
                var distance = Math.sqrt(vertex.x * vertex.x + vertex.y * vertex.y) / width;
                vertex.z = (1 - distance * distance * distance) * width/-2;
                vertex.toArray(positions, i * 3);

                color.setHSL(0.01 + 0.1 * ( i / l ), 1.0, 0.5);
                color.toArray(colors, i * 3);

                states[i] = 0;
            }

            var geometry = new THREE.BufferGeometry();
            geometry.addAttribute('position', new THREE.BufferAttribute(positions, 3));
            geometry.addAttribute('cellState', new THREE.BufferAttribute(states, 1));
            geometry.addAttribute('customColor', new THREE.BufferAttribute(colors, 3));

            var material = new THREE.ShaderMaterial({
                uniforms: {
                    color: {type: "c", value: new THREE.Color(0xffffff)},
                    texture: {type: "t", value: THREE.ImageUtils.loadTexture("disc.png")}
                },
                vertexShader: this.shaders.vertex,
                fragmentShader: this.shaders.fragment,
                alphaTest: 0.5
            });

            particles = new THREE.Points(geometry, material);
            scene.add(particles);

            renderer.setPixelRatio(window.devicePixelRatio);
            renderer.setSize(window.innerWidth, window.innerHeight);
            container.appendChild(renderer.domElement);

            controls = new Controls(camera, renderer, particles);

            window.addEventListener('resize', this.onWindowResize, false);

            this.animate();
        },

        onWindowResize : function () {
            camera.aspect = window.innerWidth / window.innerHeight;
            camera.updateProjectionMatrix();
            renderer.setSize(window.innerWidth, window.innerHeight);
        },

        animate : function () {
            requestAnimationFrame(this.animate);
            controls.update();

            this.render();
        },

        render : function () {
            if (cells) {
                var cellState = particles.geometry.attributes.cellState;

                if(cellState.array.length != cells.length) {
                    throw "Wrong length!";
                }

                cellState.array = cells;
                cellState.needsUpdate = true;
                cells = null;
            }

            renderer.render(scene, camera);
            controls.render();
        },

        togglePlay : function () {
            socket.websocket.send(this.play ? "stop" : "start");
            this.play = ! this.play;
        },

        send : function (t) {
            socket.websocket.send(t);
        }
    }
});
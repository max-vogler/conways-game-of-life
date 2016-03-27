function Controls() {
    var controls = new THREE.OrbitControls(camera, renderer.domElement);
    controls.enableZoom = true;

    var range = Math.PI / 5;
    controls.minPolarAngle = 0.5 * Math.PI - range;
    controls.maxPolarAngle = 0.5 * Math.PI + range;
    controls.minAzimuthAngle = -range;
    controls.maxAzimuthAngle = +range;
    controls.minDistance = 0;
    controls.maxDistance = 400;

    var mouse = new THREE.Vector2();
    var raycaster = new THREE.Raycaster();
    var intersects;
    var intersectedIndex;
    var clickActive = false;
    var mode;

    function onDocumentMouseMove(event) {
        event.preventDefault();

        mouse.x = ( event.clientX / window.innerWidth ) * 2 - 1;
        mouse.y = -( event.clientY / window.innerHeight ) * 2 + 1;
    }

    function onDocumentMouseDown(event) {
        clickActive = true;
    }

    function onDocumentMouseUp(event) {
        clickActive = false;
    }

    document.addEventListener( 'mousemove', onDocumentMouseMove, false );
    document.addEventListener( 'mouseup', onDocumentMouseUp, false );
    document.addEventListener( 'mousedown', onDocumentMouseDown, false );

    this.setMode = function(newMode) {
        mode = newMode;
        controls.enablePan = mode == "view";
        controls.enableRotate = mode == "view";

        console.log(controls.enablePan);
    };

    this.update = function () {
        controls.update();
    };

    this.render = function () {
        if(mode === "view" || !clickActive) {
            return;
        }

        raycaster.setFromCamera( mouse, camera );
        intersects = raycaster.intersectObject( particles );

        var attributes = particles.geometry.attributes;

        if ( intersects.length > 0 ) {
            if ( intersectedIndex != intersects[ 0 ].index ) {
                attributes.cellState.array[ intersectedIndex ] = mode == "add" ? 1 : 0;
                intersectedIndex = intersects[ 0 ].index;
                attributes.cellState.array[ intersectedIndex ] = mode == "add" ? 1 : 0;
                attributes.cellState.needsUpdate = true;

                this.onClick(intersectedIndex % size, parseInt(intersectedIndex / size));
            }

        }

    };

    this.onClick = function (x, y){
        var cell = mode == "add" ? 1 : 0;
        socket.send("set,"+x+","+y+","+cell);
        console.log("set,"+x+","+y+","+cell);
    };

    this.setMode("view");
}
/**
 * Creates map, draws paths, binds events.
 * @constructor
 * @param {Object} params Parameters to initialize map with.
 * @param {String} params.map Name of the map in the format <code>territory_proj_lang</code> where <code>territory</code> is a unique code or name of the territory which the map represents (ISO 3166 alpha 2 standard is used where possible), <code>proj</code> is a name of projection used to generate representation of the map on the plane (projections are named according to the conventions of proj4 utility) and <code>lang</code> is a code of the language, used for the names of regions.
 * @param {String} params.backgroundColor Background color of the map in CSS format.
 * @param {Boolean} params.zoomOnScroll When set to true map could be zoomed using mouse scroll. Default value is <code>true</code>.
 * @param {Number} params.zoomMax Indicates the maximum zoom ratio which could be reached zooming the map. Default value is <code>8</code>.
 * @param {Number} params.zoomMin Indicates the minimum zoom ratio which could be reached zooming the map. Default value is <code>1</code>.
 * @param {Number} params.zoomStep Indicates the multiplier used to zoom map with +/- buttons. Default value is <code>1.6</code>.
 * @param {Boolean} params.regionsSelectable When set to true regions of the map could be selected. Default value is <code>false</code>.
 * @param {Boolean} params.markersSelectable When set to true markers on the map could be selected. Default value is <code>false</code>.
 * @param {Object} params.regionStyle Set the styles for the map's regions. Each region or marker has four states: <code>initial</code> (default state), <code>hover</code> (when the mouse cursor is over the region or marker), <code>selected</code> (when region or marker is selected), <code>selectedHover</code> (when the mouse cursor is over the region or marker and it's selected simultaneously). Styles could be set for each of this states. Default value for that parameter is:
<pre>{
  initial: {
    fill: 'white',
    "fill-opacity": 1,
    stroke: 'none',
    "stroke-width": 0,
    "stroke-opacity": 1
  },
  hover: {
    "fill-opacity": 0.8
  },
  selected: {
    fill: 'yellow'
  },
  selectedHover: {
  }
}</pre>
 * @param {Object} params.markerStyle Set the styles for the map's markers. Any parameter suitable for <code>regionStyle</code> could be used as well as numeric parameter <code>r</code> to set the marker's radius. Default value for that parameter is:
<pre>{
  initial: {
    fill: 'grey',
    stroke: '#505050',
    "fill-opacity": 1,
    "stroke-width": 1,
    "stroke-opacity": 1,
    r: 5
  },
  hover: {
    stroke: 'black',
    "stroke-width": 2
  },
  selected: {
    fill: 'blue'
  },
  selectedHover: {
  }
}</pre>
 * @param {Object|Array} params.markers Set of markers to add to the map during initialization. In case of array is provided, codes of markers will be set as string representations of array indexes. Each marker is represented by <code>latLng</code> (array of two numeric values), <code>name</code> (string which will be show on marker's label) and any marker styles.
 * @param {Object} params.series Object with two keys: <code>markers</code> and <code>regions</code>. Each of which is an array of series configs to be applied to the respective map elements. See <a href="jvm.DataSeries.html">DataSeries</a> description for a list of parameters available.
 * @param {Object|String} params.focusOn This parameter sets the initial position and scale of the map viewport. It could be expressed as a string representing region which should be in focus or an object representing coordinates and scale to set. For example to focus on the center of the map at the double scale you can provide the following value:
<pre>{
  x: 0.5,
  y: 0.5,
  scale: 2
}</pre>
 * @param {Function} params.onRegionLabelShow <code>(Event e, Object label, String code)</code> Will be called right before the region label is going to be shown.
 * @param {Function} params.onRegionOver <code>(Event e, String code)</code> Will be called on region mouse over event.
 * @param {Function} params.onRegionOut <code>(Event e, String code)</code> Will be called on region mouse out event.
 * @param {Function} params.onRegionClick <code>(Event e, String code)</code> Will be called on region click event.
 * @param {Function} params.onRegionSelected <code>(Event e, String code, Boolean isSelected, Array selectedRegions)</code> Will be called when region is (de)selected. <code>isSelected</code> parameter of the callback indicates whether region is selected or not. <code>selectedRegions</code> contains codes of all currently selected regions.
 * @param {Function} params.onMarkerLabelShow <code>(Event e, Object label, String code)</code> Will be called right before the marker label is going to be shown.
 * @param {Function} params.onMarkerOver <code>(Event e, String code)</code> Will be called on marker mouse over event.
 * @param {Function} params.onMarkerOut <code>(Event e, String code)</code> Will be called on marker mouse out event.
 * @param {Function} params.onMarkerClick <code>(Event e, String code)</code> Will be called on marker click event.
 * @param {Function} params.onMarkerSelected <code>(Event e, String code, Boolean isSelected, Array selectedMarkers)</code> Will be called when marker is (de)selected. <code>isSelected</code> parameter of the callback indicates whether marker is selected or not. <code>selectedMarkers</code> contains codes of all currently selected markers.
 */
jvm.WorldMap = function(params) {
  var map = this,
      e;

  this.params = $.extend(true, {}, jvm.WorldMap.defaultParams, params);
  this.mapData = jvm.WorldMap.maps[this.params.map];
  this.markers = {};
  this.regions = {};
  this.regionsColors = {};
  this.regionsData = {};

  this.container = this.params.container;
  this.container.css({
    position: 'relative',
    overflow: 'hidden'
  });

  this.defaultWidth = this.mapData.width;
  this.defaultHeight = this.mapData.height;

  this.setBackgroundColor(this.params.backgroundColor);

  $(window).resize(function(){
    map.setSize();
  });

  for (e in jvm.WorldMap.apiEvents) {
    if (this.params[e]) {
      this.container.bind(jvm.WorldMap.apiEvents[e]+'.jvectormap', this.params[e]);
    }
  }

  this.canvas = new jvm.VectorCanvas(this.params.container[0], this.width, this.height);

  if ( ('ontouchstart' in window) || (window.DocumentTouch && document instanceof DocumentTouch) ) {
    this.bindContainerTouchEvents();
  } else {
    this.bindContainerEvents();
  }
  this.bindElementEvents();
  this.createLabel();
  this.bindZoomButtons();
  this.createRegions();
  this.createMarkers(this.params.markers || {});

  this.setSize();

  if (this.params.focusOn) {
    if (typeof this.params.focusOn === 'object') {
      this.setFocus.call(this, this.params.focusOn.scale, this.params.focusOn.x, this.params.focusOn.y);
    } else {
      this.setFocus.call(this, this.params.focusOn);
    }
  }

  if (this.params.series) {
    this.createSeries();
  }
};

jvm.WorldMap.prototype = {
  transX: 0,
  transY: 0,
  scale: 1,
  baseTransX: 0,
  baseTransY: 0,
  baseScale: 1,

  width: 0,
  height: 0,

  /**
   * Set background color of the map.
   * @param {String} backgroundColor Background color in CSS format.
   */
  setBackgroundColor: function(backgroundColor) {
    this.container.css('background-color', backgroundColor);
  },

  resize: function() {
    var curBaseScale = this.baseScale;
    if (this.width / this.height > this.defaultWidth / this.defaultHeight) {
      this.baseScale = this.height / this.defaultHeight;
      this.baseTransX = Math.abs(this.width - this.defaultWidth * this.baseScale) / (2 * this.baseScale);
    } else {
      this.baseScale = this.width / this.defaultWidth;
      this.baseTransY = Math.abs(this.height - this.defaultHeight * this.baseScale) / (2 * this.baseScale);
    }
    this.scale *= this.baseScale / curBaseScale;
    this.transX *= this.baseScale / curBaseScale;
    this.transY *= this.baseScale / curBaseScale;
  },

  setSize: function(){
    this.width = this.container.width();
    this.height = this.container.height();
    this.resize();
    this.canvas.setSize(this.width, this.height);
    this.applyTransform();
  },

  /**
   * Reset colors of the regions, show the map with the initial zoom.
   */
  reset: function() {
    var key;

    this.countryTitle.reset();
    for (key in this.regions) {
      this.regions[key].element.setStyle('fill', jvm.WorldMap.defaultColor);
    }
    this.scale = this.baseScale;
    this.transX = this.baseTransX;
    this.transY = this.baseTransY;
    this.applyTransform();
  },

  applyTransform: function() {
    var maxTransX, maxTransY, minTransX, minTransY;
    if (this.defaultWidth * this.scale <= this.width) {
      maxTransX = (this.width - this.defaultWidth * this.scale) / (2 * this.scale);
      minTransX = (this.width - this.defaultWidth * this.scale) / (2 * this.scale);
    } else {
      maxTransX = 0;
      minTransX = (this.width - this.defaultWidth * this.scale) / this.scale;
    }

    if (this.defaultHeight * this.scale <= this.height) {
      maxTransY = (this.height - this.defaultHeight * this.scale) / (2 * this.scale);
      minTransY = (this.height - this.defaultHeight * this.scale) / (2 * this.scale);
    } else {
      maxTransY = 0;
      minTransY = (this.height - this.defaultHeight * this.scale) / this.scale;
    }

    if (this.transY > maxTransY) {
      this.transY = maxTransY;
    } else if (this.transY < minTransY) {
      this.transY = minTransY;
    }
    if (this.transX > maxTransX) {
      this.transX = maxTransX;
    } else if (this.transX < minTransX) {
      this.transX = minTransX;
    }

    this.canvas.applyTransformParams(this.scale, this.transX, this.transY);

    if (this.markers) {
      this.repositionMarkers();
    }
  },

  bindContainerEvents: function(){
    var mouseDown = false,
        oldPageX,
        oldPageY,
        map = this;

    this.container.mousemove(function(e){
      if (mouseDown) {
        map.transX -= (oldPageX - e.pageX) / map.scale;
        map.transY -= (oldPageY - e.pageY) / map.scale;

        map.applyTransform();

        oldPageX = e.pageX;
        oldPageY = e.pageY;
      }
      return false;
    }).mousedown(function(e){
      mouseDown = true;
      oldPageX = e.pageX;
      oldPageY = e.pageY;
      return false;
    }).mouseup(function(){
      mouseDown = false;
      return false;
    });

    if (this.params.zoomOnScroll) {
      this.container.mousewheel(function(event, delta, deltaX, deltaY) {
        var offset = $(map.container).offset(),
            centerX = event.pageX - offset.left,
            centerY = event.pageY - offset.top,
            zoomStep = Math.pow(1.3, deltaY);

        map.label.hide();

        map.setScale(map.scale * zoomStep, centerX, centerY);
        event.preventDefault();
      });
    }
  },

  bindContainerTouchEvents: function(){
    var touchStartScale,
        touchStartTransX,
        touchStartTransY,
        map = this,
        touchX,
        touchY,
        centerTouchX,
        centerTouchY;

    $(this.container).bind('gesturestart', function(e){
      touchStartScale = map.scale;
      touchStartTransX = map.transX;
      touchStartTransY = map.transY;
      return false;
    });
    $(this.container).bind('gesturechange', function(e){
      var zoomStep = e.originalEvent.scale;

      map.setScale(
        touchStartScale * zoomStep,
        centerTouchX,
        centerTouchY
      );
      map.label.hide();
      return false;
    });
    $(this.container).bind('touchstart', function(e){
      var touches = e.originalEvent.touches;
      if (touches.length == 2) {
        if (touches[0].pageX > touches[1].pageX) {
          centerTouchX = touches[1].pageX + (touches[0].pageX - touches[1].pageX) / 2;
        } else {
          centerTouchX = touches[0].pageX + (touches[1].pageX - touches[0].pageX) / 2;
        }
        if (touches[0].pageY > touches[1].pageY) {
          centerTouchY = touches[1].pageY + (touches[0].pageY - touches[1].pageY) / 2;
        } else {
          centerTouchY = touches[0].pageY + (touches[1].pageY - touches[0].pageY) / 2;
        }
      }
      touchX = e.originalEvent.touches[0].pageX;
      touchY = e.originalEvent.touches[0].pageY;
    });
    $(this.container).bind('touchmove', function(e){
      var touch;
      if (e.originalEvent.touches.length == 1 && touchX && touchY) {
        touch = e.originalEvent.touches[0];

        map.transX -= (touchX - touch.pageX) / map.scale;
        map.transY -= (touchY - touch.pageY) / map.scale;

        map.applyTransform();

        map.label.hide();

        touchX = touch.pageX;
        touchY = touch.pageY;
      } else {
        touchX = false;
        touchY = false;
      }
      return false;
    });
  },

  bindElementEvents: function(){
    var map = this,
        mouseMoved;

    this.container.mousemove(function(){
      mouseMoved = true;
    });

    this.container.delegate('.jvectormap-region, .jvectormap-marker', 'mouseover mouseout', function(e){
      var path = this,
          type = $(this).attr('class').indexOf('jvectormap-region') === -1 ? 'marker' : 'region',
          code = type == 'region' ? $(this).attr('data-code') : $(this).attr('data-index'),
          element = type == 'region' ? map.regions[code].element : map.markers[code].element,
          labelText = type == 'region' ? map.mapData.paths[code].name : (map.markers[code].config.name || ''),
          labelShowEvent = $.Event(type+'LabelShow.jvectormap'),
          overEvent = $.Event(type+'Over.jvectormap');

      if (e.type == 'mouseover') {
        map.container.trigger(overEvent, [code]);
        if (!overEvent.isDefaultPrevented()) {
          element.setHovered(true);
        }

        map.label.text(labelText);
        map.container.trigger(labelShowEvent, [map.label, code]);
        if (!labelShowEvent.isDefaultPrevented()) {
          map.label.show();
          map.labelWidth = map.label.width();
          map.labelHeight = map.label.height();
        }
      } else {
        element.setHovered(false);
        map.label.hide();
        map.container.trigger(type+'Out.jvectormap', [code]);
      }
    });

    this.container.delegate('.jvectormap-region, .jvectormap-marker', 'mousedown', function(e){
      mouseMoved = false;
    });

    this.container.delegate('.jvectormap-region, .jvectormap-marker', 'mouseup', function(e){
      var path = this,
          type = $(this).attr('class').indexOf('jvectormap-region') === -1 ? 'marker' : 'region',
          code = type == 'region' ? $(this).attr('data-code') : $(this).attr('data-index'),
          clickEvent = $.Event(type+'Click.jvectormap'),
          element = type == 'region' ? map.regions[code].element : map.markers[code].element;

      if (!mouseMoved) {
        map.container.trigger(clickEvent, [code]);
        if ((type === 'region' && map.params.regionsSelectable) || (type === 'marker' && map.params.markersSelectable)) {
          if (!clickEvent.isDefaultPrevented()) {
            element.setSelected(!element.isSelected);
          }
        }
      }
    });
  },

  bindZoomButtons: function() {
    var map = this;

    $('<div/>').addClass('jvectormap-zoomin').text('+').appendTo(this.container);
    $('<div/>').addClass('jvectormap-zoomout').html('&#x2212;').appendTo(this.container);

    this.container.find('.jvectormap-zoomin').click(function(){
      map.setScale(map.scale * map.params.zoomStep, map.width / 2, map.height / 2);
    });
    this.container.find('.jvectormap-zoomout').click(function(){
      map.setScale(map.scale / map.params.zoomStep, map.width / 2, map.height / 2);
    });
  },

  createLabel: function(){
    var map = this;

    this.label = $('<div/>').addClass('jvectormap-label').appendTo($('body'));

    this.container.mousemove(function(e){
      if (map.label.is(':visible')) {
        map.label.css({
          left: e.pageX-15-map.labelWidth,
          top: e.pageY-15-map.labelHeight
        })
      }
    });
  },

  setScale: function(scale, anchorX, anchorY, isCentered) {
    var zoomStep;

    if (scale > this.params.zoomMax * this.baseScale) {
      scale = this.params.zoomMax * this.baseScale;
    } else if (scale < this.params.zoomMin * this.baseScale) {
      scale = this.params.zoomMin * this.baseScale;
    }

    if (typeof anchorX != 'undefined' && typeof anchorY != 'undefined') {
      zoomStep = scale / this.scale;
      if (isCentered) {
        this.transX = anchorX + this.defaultWidth * (this.width / (this.defaultWidth * scale)) / 2;
        this.transY = anchorY + this.defaultHeight * (this.height / (this.defaultHeight * scale)) / 2;
      } else {
        this.transX -= (zoomStep - 1) / scale * anchorX;
        this.transY -= (zoomStep - 1) / scale * anchorY;
      }
    }

    this.scale = scale;
    this.applyTransform();
  },

  /**
   * Set the map's viewport to the specific point and set zoom of the map to the specific level. Point and zoom level could be defined in two ways: using the code of some region to focus on or a central point and zoom level as numbers.
   * @param {Number|String} scale|regionCode If the first parameter of this method is a string and there is a region with the same code, the viewport will be set to show the whole region. Otherwise if the first parameter is a number, the viewport will be to show the map with provided scale.
   * @param {Number} centerX Number from 0 to 1 specifying the horizontal coordinate of the central point of the viewport.
   * @param {Number} centerY Number from 0 to 1 specifying the vertical coordinate of the central point of the viewport.
   */
  setFocus: function(scale, centerX, centerY){
    var bbox;

    if (typeof scale === 'string' && this.regions[scale]) {
      bbox = this.regions[scale].element.getBBox();
      this.setScale(
        Math.min(this.width / bbox.width, this.height / bbox.height),
        - (bbox.x + bbox.width / 2),
        - (bbox.y + bbox.height / 2),
        true
      );
    } else {
      this.setScale(scale, - centerX * this.defaultWidth, - centerY * this.defaultHeight, true);
    }
  },

  /**
   * Return the codes of currently selected regions.
   * @returns {Array}
   */
  getSelectedRegions: function(){
    var key,
        selected = [];

    for (key in this.regions) {
      if (this.regions[key].element.isSelected) {
        selected.push(key);
      }
    }
    return selected;
  },

  /**
   * Return the codes of currently selected markers.
   * @returns {Array}
   */
  getSelectedMarkers: function(){
    var i,
        selected = [];

    for (i in this.markers) {
      if (this.markers[i].element.isSelected) {
        selected.push(i);
      }
    }
    return selected;
  },

  /**
   * Return the instance of WorldMap. Useful when instantiated as a jQuery plug-in.
   * @returns {WorldMap}
   */
  getMapObject: function(){
    return this;
  },

  /**
   * Return the name of the region by region code.
   * @returns {String}
   */
  getRegionName: function(code){
    return this.mapData.paths[code].name;
  },

  createRegions: function(){
    var key,
        region,
        map = this;

    for (key in this.mapData.paths) {
      region = this.canvas.addPath({
        d: this.mapData.paths[key].path,
        "data-code": key
      }, $.extend({}, this.params.regionStyle));
      $(region.node).bind('selected', function(e, isSelected){
        map.container.trigger('regionSelected.jvectormap', [$(this).attr('data-code'), isSelected, map.getSelectedRegions()]);
      });
      region.addClass('jvectormap-region');
      this.regions[key] = {
        element: region,
        config: this.mapData.paths[key]
      };
    }
  },

  createMarkers: function(markers) {
    var group = this.canvas.addGroup(),
        i,
        marker,
        point,
        markerConfig,
        markersArray,
        map = this;

    if ($.isArray(markers)) {
      markersArray = markers.slice();
      markers = {};
      for (i = 0; i < markersArray.length; i++) {
        markers[i] = markersArray[i];
      }
    }

    for (i in markers) {
      markerConfig = markers[i] instanceof Array ? {latLng: markers[i]} : markers[i];
      point = this.latLngToPoint.apply(this, markerConfig.latLng || [0, 0]);

      marker = this.canvas.addCircle({
        "data-index": i,
        cx: point.x,
        cy: point.y
      }, $.extend(true, {}, this.params.markerStyle, {initial: markerConfig.style || {}}), group);
      marker.addClass('jvectormap-marker');
      $(marker.node).bind('selected', function(e, isSelected){
        map.container.trigger('markerSelected.jvectormap', [$(this).attr('data-index'), isSelected, map.getSelectedMarkers()]);
      });
      if (this.markers[i]) {
        this.removeMarkers([i]);
      }
      this.markers[i] = {element: marker, config: markerConfig};
    }
  },

  repositionMarkers: function() {
    var i,
        point;

    for (i in this.markers) {
      point = this.latLngToPoint.apply(this, this.markers[i].config.latLng);
      this.markers[i].element.setStyle({cx: point.x, cy: point.y});
    }
  },

  /**
   * Add one marker to the map.
   * @param {String} key Marker unique code.
   * @param {Object} marker Marker configuration parameters.
   * @param {Array} seriesData Values to add to the data series.
   */
  addMarker: function(key, marker, seriesData){
    var markers = {},
        data = [],
        values,
        i;

    markers[key] = marker;

    for (i = 0; i < seriesData.length; i++) {
      values = {};
      values[key] = seriesData[i];
      data.push(values);
    }
    this.addMarkers(markers, data);
  },

  /**
   * Add set of marker to the map.
   * @param {Object|Array} markers Markers to add to the map. In case of array is provided, codes of markers will be set as string representations of array indexes.
   * @param {Array} seriesData Values to add to the data series.
   */
  addMarkers: function(markers, seriesData){
    var i;

    this.createMarkers(markers);
    for (i = 0; i < seriesData.length; i++) {
      this.series.markers[i].setValues(seriesData[i] || {});
    };
  },

  /**
   * Remove some markers from the map.
   * @param {Array} markers Array of marker codes to be removed.
   */
  removeMarkers: function(markers){
    var i;

    for (i = 0; i < markers.length; i++) {
      this.markers[ markers[i] ].element.remove();
      delete this.markers[ markers[i] ];
    };
  },

  /**
   * Remove all markers from the map.
   */
  clearMarkers: function(){
    var i,
        markers = [];

    for (i in this.markers) {
      markers.push(i);
    }
    this.removeMarkers(markers)
  },

  /**
   * Converts coordinates expressed as latitude and longtitude to the coordinates in pixels on the map.
   * @param {Number} lat Latitide of point in degrees.
   * @param {Number} lng Longtitude of point in degrees.
   */
  latLngToPoint: function(lat, lng) {
    var point,
        proj = jvm.WorldMap.maps[this.params.map].projection,
        centralMeridian = proj.centralMeridian,
        width = this.width - this.baseTransX * 2 * this.baseScale,
        height = this.height - this.baseTransY * 2 * this.baseScale,
        inset,
        bbox,
        scaleFactor = this.scale / this.baseScale;

    if (lng < (-180 + centralMeridian)) {
      lng += 360;
    }

    point = jvm.Proj[proj.type](lat, lng, centralMeridian);

    inset = this.getInsetForPoint(point.x, point.y);
    if (inset) {
      bbox = inset.bbox;

      point.x = (point.x - bbox[0].x) / (bbox[1].x - bbox[0].x) * inset.width * this.scale;
      point.y = (point.y - bbox[0].y) / (bbox[1].y - bbox[0].y) * inset.height * this.scale;

      return {
        x: point.x + this.transX*this.scale + inset.left*this.scale,
        y: point.y + this.transY*this.scale + inset.top*this.scale
      };
     } else {
       return {x: 0, y: 0};
     }
  },

  getInsetForPoint: function(x, y){
    var insets = jvm.WorldMap.maps[this.params.map].insets,
        i,
        bbox;

    for (i = 0; i < insets.length; i++) {
      bbox = insets[i].bbox;
      if (x > bbox[0].x && x < bbox[1].x && y > bbox[0].y && y < bbox[1].y) {
        return insets[i];
      }
    }
  },

  createSeries: function(){
    var i,
        key;

    this.series = {
      markers: [],
      regions: []
    };

    for (key in this.params.series) {
      for (i = 0; i < this.params.series[key].length; i++) {
        this.series[key][i] = new jvm.DataSeries(
          this.params.series[key][i],
          this[key]
        );
      }
    }
  }
};

jvm.WorldMap.maps = {};
jvm.WorldMap.circumference = 40075017;
jvm.WorldMap.defaultParams = {
  map: 'world_mill_en',
  backgroundColor: '#505050',
  zoomOnScroll: true,
  zoomMax: 8,
  zoomMin: 1,
  zoomStep: 1.6,
  regionsSelectable: false,
  markersSelectable: false,
  regionStyle: {
    initial: {
      fill: 'white',
      "fill-opacity": 1,
      stroke: 'none',
      "stroke-width": 0,
      "stroke-opacity": 1
    },
    hover: {
      "fill-opacity": 0.8
    },
    selected: {
      fill: 'yellow'
    },
    selectedHover: {
    }
  },
  markerStyle: {
    initial: {
      fill: 'grey',
      stroke: '#505050',
      "fill-opacity": 1,
      "stroke-width": 1,
      "stroke-opacity": 1,
      r: 5
    },
    hover: {
      stroke: 'black',
      "stroke-width": 2
    },
    selected: {
      fill: 'blue'
    },
    selectedHover: {
    }
  }
};
jvm.WorldMap.apiEvents = {
  onRegionLabelShow: 'regionLabelShow',
  onRegionOver: 'regionOver',
  onRegionOut: 'regionOut',
  onRegionClick: 'regionClick',
  onRegionSelected: 'regionSelected',
  onMarkerLabelShow: 'markerLabelShow',
  onMarkerOver: 'markerOver',
  onMarkerOut: 'markerOut',
  onMarkerClick: 'markerClick',
  onMarkerSelected: 'markerSelected',
};
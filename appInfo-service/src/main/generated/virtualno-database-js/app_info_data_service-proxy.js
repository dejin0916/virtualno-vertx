/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

/// <reference path="./app_info_data_service-proxy.d.ts" />

/** @module virtualno-database-js/app_info_data_service */
!function (factory) {
  if (typeof require === 'function' && typeof module !== 'undefined') {
    factory();
  } else if (typeof define === 'function' && define.amd) {
    // AMD loader
    define('virtualno-database-js/app_info_data_service-proxy', [], factory);
  } else {
    // plain old include
    AppInfoDataService = factory();
  }
}(function () {

  /**
   @class
  */
  var AppInfoDataService = function(eb, address) {
    var j_eb = eb;
    var j_address = address;
    var closed = false;
    var that = this;
    var convCharCollection = function(coll) {
      var ret = [];
      for (var i = 0;i < coll.length;i++) {
        ret.push(String.fromCharCode(coll[i]));
      }
      return ret;
    };

    /**

     @public
     @param appId {string} 
     @return {todo}
     */
    this.fetchAppByAppId =  function(appId) {
      var __args = arguments;
      if (__args.length === 1 && typeof __args[0] === 'string') {
        if (closed) {
          throw new Error('Proxy is closed');
        }
        j_eb.send(j_address, {"appId":__args[0]}, {"action":"fetchAppByAppId"});
        return;
      } else throw new TypeError('function invoked with invalid arguments');
    };

    /**

     @public
     @param appId {string} 
     @param appKey {string} 
     @param secret {string} 
     @return {todo}
     */
    this.saveApp =  function(appId, appKey, secret) {
      var __args = arguments;
      if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && typeof __args[2] === 'string') {
        if (closed) {
          throw new Error('Proxy is closed');
        }
        j_eb.send(j_address, {"appId":__args[0], "appKey":__args[1], "secret":__args[2]}, {"action":"saveApp"});
        return;
      } else throw new TypeError('function invoked with invalid arguments');
    };

    /**

     @public

     @return {todo}
     */
    this.fetchAllApps =  function() {
      var __args = arguments;
      if (__args.length === 0) {
        if (closed) {
          throw new Error('Proxy is closed');
        }
        j_eb.send(j_address, {}, {"action":"fetchAllApps"});
        return;
      } else throw new TypeError('function invoked with invalid arguments');
    };

    /**

     @public
     @param appId {string} 
     @return {todo}
     */
    this.deleteApp =  function(appId) {
      var __args = arguments;
      if (__args.length === 1 && typeof __args[0] === 'string') {
        if (closed) {
          throw new Error('Proxy is closed');
        }
        j_eb.send(j_address, {"appId":__args[0]}, {"action":"deleteApp"});
        return;
      } else throw new TypeError('function invoked with invalid arguments');
    };

    /**

     @public
     @param virtualNoApp {Object} 
     @return {todo}
     */
    this.createApp =  function(virtualNoApp) {
      var __args = arguments;
      if (__args.length === 1 && (typeof __args[0] === 'object' && __args[0] != null)) {
        if (closed) {
          throw new Error('Proxy is closed');
        }
        j_eb.send(j_address, {"virtualNoApp":__args[0]}, {"action":"createApp"});
        return;
      } else throw new TypeError('function invoked with invalid arguments');
    };

  };

  if (typeof exports !== 'undefined') {
    if (typeof module !== 'undefined' && module.exports) {
      exports = module.exports = AppInfoDataService;
    } else {
      exports.AppInfoDataService = AppInfoDataService;
    }
  } else {
    return AppInfoDataService;
  }
});
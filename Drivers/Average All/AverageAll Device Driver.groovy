/**
 *  Average Virtual Illuminance/Temperature/Humidity/Pressure/Motion Device
 *
 *  Copyright 2018 Andrew Parker
 *
 *  
 *  
 *  
 *
 *  
 *  This driver is free!
 *
 *  Donations to support development efforts are welcomed via: 
 *
 *  Paypal at: https://www.paypal.me/smartcobra
 *  
 *
 *  I'm very happy for you to use this driver without a donation, but if you find it useful
 *  then it would be nice to get a 'shout out' on the forum! -  @Cobra
 *  Have an idea to make this driver better?  - Please let me know :)
 *
 *  
 *
 *-------------------------------------------------------------------------------------------------------------------
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *-------------------------------------------------------------------------------------------------------------------
 *
 *  If modifying this project, please keep the above header intact and add your comments/credits below - Thank you! -  @Cobra
 *
 *-------------------------------------------------------------------------------------------------------------------
 *
 *  Last Update 15/11/2018
 *
 *  V1.3.3 - Debug - Typo in lastDeviceHumidity
 *  V1.3.2 - Debug UI
 *  V1.3.1 - Debug 'LastDevice'
 *  V1.3.0 - Added switchable logging
 *  V1.2.0 - Added 'Motion' average
 *  V1.1.0 - Debug and added 'last device' separation - one for each attribute
 *  V1.0.0 - POC
 */



metadata {
	definition (name: "Average All Device", namespace: "Cobra", author: "Cobra") {
		capability "Illuminance Measurement"
		capability "Relative Humidity Measurement"
        capability "Temperature Measurement"
        capability "Motion Sensor"
		capability "Sensor"
        
        command "setTemperature", ["decimal"]
        command "setHumidity", ["decimal"]
        command "setLux", ["decimal"]
        command "setPressure", ["decimal"]
        
        command "lastDeviceLux"
        command "lastDeviceTemperature"
        command "lastDeviceHumidity"
        command "lastDevicePressure"
        command "lastDeviceMotion"
        command "setMotion", ["string"]
//      command "active"
//		command "inactive"
        
        attribute  " ", "string"
        attribute  "LastDeviceLux", "string"
        attribute  "LastDeviceTemperature", "string"
        attribute  "LastDevicePressure", "string"
        attribute  "LastDeviceHumidity", "string"
        attribute  "LastDeviceMotion", "string"
        
//        attribute "DriverAuthor", "string"
        attribute "DriverVersion", "string"
        attribute "DriverStatus", "string"
        attribute "DriverUpdate", "string" 
        attribute "pressure", "string"
	}
    
    
  preferences() {
    
     section("") {
   		input "unitSelect", "enum",  title: "Temperature Units (If using temperature)", required: true, options: ["C", "F"] 
   		input "pressureUnit", "enum", title: "Pressure Unit (If using pressure)", required:true, options: ["inhg", "mbar"]
        input "debugMode", "bool", title: "Enable logging", required: true, defaultValue: false  
 		}  
 }   
}

def installed(){
    initialise()
}

def updated(){
    initialise()
}



def initialise() {
    logCheck()
    version()
    if(state.TemperatureUnit == null){ state.TemperatureUnit = "F"}
    else{state.TemperatureUnit = unitSelect}
    if(pressureUnit == null){state.PressureUnit = "inhg"}
    else{state.PressureUnit = pressureUnit}
    
    
}

def lastDeviceLux(dev1){  
    state.LastDeviceLux = dev1
	sendEvent(name: "LastDeviceLux", value: state.LastDeviceLux , isStateChange: true)
}
def LastDeviceHumidity(dev2){  
	state.LastDeviceHumid = dev2
    sendEvent(name: "LastDeviceHumidity", value: state.LastDeviceHumid , isStateChange: true)
}
def lastDevicePressure(dev3){ 
	state.LastDevicePressure = dev3
    sendEvent(name: "LastDevicePressure", value: state.LastDevicePressure , isStateChange: true)
}
def lastDeviceTemperature(dev4){ 
	state.LastDeviceTemperature = dev4
    sendEvent(name: "LastDeviceTemperature", value: state.LastDeviceTemperature , isStateChange: true)
}

def lastDeviceMotion(dev5){ 
	state.LastDeviceMotion = dev5
    sendEvent(name: "LastDeviceMotion", value: state.LastDeviceMotion , isStateChange: true)
}

def active(motion1) {
//	state.ReceivedMotion = motion1
    LOGDEBUG( "Setting motion for ${device.displayName} from external input ($state.LastDeviceMotion), Motion = ${motion1}.")
	sendEvent(name: "motion", value: 'active', isStateChange: true)
}

def inactive(motion1) {
//	state.ReceivedMotion = motion1
    LOGDEBUG( "Setting motion for ${device.displayName} from external input ($state.LastDeviceMotion), Motion = ${motion1}.")
	sendEvent(name: "motion", value: 'inactive', isStateChange: true)
}

def setMotion(motion1){
 state.ReceivedMotion = motion1
    LOGDEBUG( "Setting motion for ${device.displayName} from external input ($state.LastDeviceMotion), Motion = ${state.ReceivedMotion}.")
   sendEvent(name: "motion", value: state.ReceivedMotion, isStateChange: true) 
}

def setHumidity(hum1) {
	state.ReceivedHumidity = hum1
    LOGDEBUG( "Setting humidity for ${device.displayName} from external input ($state.LastDeviceHumid), Humidity = ${state.ReceivedHumidity}.")
	sendEvent(name: "humidity", value: state.ReceivedHumidity, unit: "%", isStateChange: true)
}


def setLux(ilum1) {
    state.ReceivedIlluminance = ilum1
    LOGDEBUG("Setting illuminance for ${device.displayName} from external input ($state.LastDeviceLux), Illuminance = ${state.ReceivedIlluminance}.")
	sendEvent(name: "illuminance", value: state.ReceivedIlluminance, unit: "lux", isStateChange: true)
}


def setTemperature(temp1){ 
 state.ReceivedTemp = temp1
	LOGDEBUG( "Setting temperature for ${device.displayName} from external input ($state.LastDeviceTemperature), Temperature = ${state.ReceivedTemp}.")
    sendEvent(name:"temperature", value: state.ReceivedTemp , unit: state.TemperatureUnit, isStateChange: true)
   
}

def setPressure(pres1){ 
 state.ReceivedPressure = pres1
	LOGDEBUG("Setting pressure for ${device.displayName} from external input ($state.LastDevicePressure), Pressure = ${state.ReceivedPressure}.")
    sendEvent(name:"pressure", value: state.ReceivedPressure , unit: state.PressureUnit, isStateChange: true)
    
}
def logCheck(){
state.checkLog = debugMode
if(state.checkLog == true){
log.info "All Logging Enabled"
}
else if(state.checkLog == false){
log.info "Further Logging Disabled"
}

}
def LOGDEBUG(txt){
    try {
    	if (settings.debugMode) { log.debug("Device Version: ${state.Version}) - ${txt}") }
    } catch(ex) {
    	log.error("LOGDEBUG unable to output requested data!")
    }
}


def version(){
    unschedule()
    schedule("0 0 8 ? * FRI *", updateCheck)  
    updateCheck()
}

def updateCheck(){
    setVersion()
	def paramsUD = [uri: "http://update.hubitat.uk/cobra.json" ]  
       	try {
        httpGet(paramsUD) { respUD ->
 //  log.warn " Version Checking - Response Data: ${respUD.data}"   // Troubleshooting Debug Code **********************
       		def copyrightRead = (respUD.data.copyright)
       		state.Copyright = copyrightRead
            def newVerRaw = (respUD.data.versions.Driver.(state.InternalName))
            def newVer = (respUD.data.versions.Driver.(state.InternalName).replace(".", ""))
       		def currentVer = state.Version.replace(".", "")
      		state.UpdateInfo = (respUD.data.versions.UpdateInfo.Driver.(state.InternalName))
            state.author = (respUD.data.author)
            def icon = (respUD.data.icon)
            sendEvent(name: " ", value: respUD.data.icon + '<br>' +state.Copyright +'<br>'+'<br>', isStateChange: true)
            
		if(newVer == "NLS"){
            state.Status = "<b>** This driver is no longer supported by $state.author  **</b>"       
            log.warn "** This driver is no longer supported by $state.author **"      
      		}           
		else if(currentVer < newVer){
        	state.Status = "<b>New Version Available (Version: $newVerRaw)</b>"
        	log.warn "** There is a newer version of this driver available  (Version: $newVerRaw) **"
        	log.warn "** $state.UpdateInfo **"
       		} 
		else{ 
      		state.Status = "Current"
      		log.info "You are using the current version of this driver"
       		}
      					}
        	} 
        catch (e) {
        	log.error "Something went wrong: CHECK THE JSON FILE AND IT'S URI -  $e"
    		}
   		if(state.Status == "Current"){
			state.UpdateInfo = "N/A"
		    sendEvent(name: "DriverUpdate", value: state.UpdateInfo, isStateChange: true)
	 	    sendEvent(name: "DriverStatus", value: state.Status, isStateChange: true)
			}
    	else{
	    	sendEvent(name: "DriverUpdate", value: state.UpdateInfo, isStateChange: true)
	     	sendEvent(name: "DriverStatus", value: state.Status, isStateChange: true)
	    }   
   
 			
    		sendEvent(name: "DriverVersion", value: state.Version, isStateChange: true)
    
    
	
}

def setVersion(){
		state.Version = "1.3.3"	
		state.InternalName = "AverageAll"   
}





















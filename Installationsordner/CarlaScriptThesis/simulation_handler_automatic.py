import glob
import os
import sys
import time
import json
import socket

try:
    sys.path.append(glob.glob('../carla/dist/carla-*%d.%d-%s.egg' % (
        sys.version_info.major,
        sys.version_info.minor,
        'win-amd64' if os.name == 'nt' else 'linux-x86_64'))[0])
except IndexError:
    pass

import carla

import argparse
import logging
import random
import _thread

def main():
    state=0
    print("")
    print("Verbindung mit Carla wird hergestellt...")
    actor_list = []
    try:
        client = carla.Client('localhost', 2000)
        client.set_timeout(10.0)
        

        world = client.get_world()
        
        # Create a TCP/IP socket
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    
        # Bind the socket to the port
        server_address = ('192.168.178.145', 22898)
        
        msg=b''
        sock.connect(server_address)
        print('succesfull')
        encoding = 'utf-8'
        
        #gameloop
        while(state<7):
            while True:
                time.sleep(5) # wait 5 secs.
                data = sock.recv(512)
                
                #parse received Message
                byte_list = list(data)
                result = "".join(map(chr, byte_list))
                print(result)
                
                try:
                    noObj, obj = result.split('{')
                    obj = "{"+obj
                    
                    #parse to json
                    msg = json.loads(obj)
                    
                    #get certain attribute
                    state=msg['action']
                    print(state)
                    if(state<8 and state >-1):
                        break
                except:
                    pass
            
            print("")
            
            if(state ==0):
                print("Verbindung erfolgreich")
                
            elif(state ==1):
                # spawnen
                vehicle1 = spawnCar(world, 8)
                actor_list.append(vehicle1)
                print(vehicle1)
                
            elif(state ==2):
                #los fahren
                drivToParkingArea(vehicle1)
                
            elif(state ==3):
                #in Registrationzone fahren
                for actor in actor_list:
                    actor.destroy()
                    actor_list.remove(actor)
                vehicle1 = spawnCar(world, 10)
                actor_list.append(vehicle1)
                driveIntoPerception(vehicle1)
                
            elif(state==4):
                #Parken
                driveToParkinglot(vehicle1)
                
            elif(state==5):
                # weg fahren
                driveAway(vehicle1)
                
            elif(state==6):
                # alle löschen
                for actor in actor_list:
                    actor.destroy()
                    actor_list.remove(actor)
           
            print("")

    finally:
        print('Simulation is vorbei! Vielen Dank für Ihr Interesse!')
        print('Räume die Simulation auf....')
        for actor in actor_list:
            actor.destroy()
        print('Fertig.')


def drivToParkingArea(vehicle1):
    print('Fahre zum Parkplatz')
    vehicle1.apply_control(carla.VehicleControl(throttle=0.7, steer=0.0))
    time.sleep(6.3)
    vehicle1.apply_control(carla.VehicleControl(throttle=0.0, steer=0.0))   
        
def spawnCar(world, spawnpoint):
    print('Auto spawnt')
    blueprint_library = world.get_blueprint_library()
    bp = blueprint_library.filter('model3')[0]
    sp1 = world.get_map().get_spawn_points()[spawnpoint]

    return world.spawn_actor(bp, sp1)  
        

def driveIntoPerception(vehicle1):
    # drive into perception Area
    print('Fahre in Registrierungs zone')
    vehicle1.apply_control(carla.VehicleControl(throttle=0.4, steer=0))
    time.sleep(4)
    vehicle1.apply_control(carla.VehicleControl(throttle=0, steer=-1.0))
    time.sleep(2.4)
    vehicle1.apply_control(carla.VehicleControl(throttle=0.0, steer=0.0))

    
def driveToParkinglot(vehicle1):
    # drive to parkinglot
    print('Ich Parke!')
    vehicle1.apply_control(carla.VehicleControl(throttle=0.35, steer=0.0))
    time.sleep(2.5)
    vehicle1.apply_control(carla.VehicleControl(throttle=0.15, steer=-0.87))
    time.sleep(3.2)
    #vehicle1.apply_control(carla.VehicleControl(throttle=0.15, steer=0.0))
    #time.sleep(2)
    vehicle1.apply_control(carla.VehicleControl(throttle=0.0, steer=0.0))



def driveAway(vehicle1):
    print('Ich fahre weg!')
    vehicle1.apply_control(carla.VehicleControl(reverse=1, throttle=-0.3, steer=0.75))
    time.sleep(5.4)
    vehicle1.apply_control(carla.VehicleControl(reverse=0, throttle=0.5, steer=0.0))
    time.sleep(6)
    vehicle1.apply_control(carla.VehicleControl(throttle=0.0, steer=0.0))

    
if __name__ == '__main__':

    try:
        main()
    except KeyboardInterrupt:
        pass
    finally:
        print('\ndone.')

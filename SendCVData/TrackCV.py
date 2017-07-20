import socket
import sys
import cv2
import numpy as np

HOST = sys.argv[1]
PORT = 6969

client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

def nothing(x):
    pass

def sendData(MSG):
    client_socket.send(MSG + "\n")

def connectClient(HOST, PORT):
    client_socket.connect((HOST, PORT))

def show_webcam():
    cam = cv2.VideoCapture(0)
    cam.set(11,0.0)
    while True:
        _, img = cam.read()
        blue = img.item(100,100,0)
        green = img.item(100,100,1)
        red = img.item(100,100,2)
        send = (`red`+":"+`green`+":"+`blue`)
        sendData(send)

def trackWithDraw(cap):

        #Take each frame
        _, frame = cap.read()

        #Convert BGR to HSV
        hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)

        #define range of green color in HSV
        lower_green = np.array([55,40,150])
        upper_green = np.array([85,255,255])

        #Threshold the HSV image to get only green colors
        mask = cv2.inRange(hsv, lower_green, upper_green)
        im2, contours, hierarchy = cv2.findContours(mask,cv2.RETR_EXTERNAL,cv2.CHAIN_APPROX_SIMPLE)

        #Make sure there are contours
        if contours:

            #Find and draw largest contour
            c = max(contours, key = cv2.contourArea)
            cv2.drawContours(frame, c, -1, (0,0,255), 3)

            #Calculate centroid using moment data
            moment = cv2.moments(c)

            #Prevent divide by zero
            if moment['m00'] > 0:
                cx = int(moment['m10']/moment['m00'])
                cy = int(moment['m01']/moment['m00'])

                #Draw marker on centroid
                cv2.drawMarker(frame, (cx,cy), (0,0,0), cv2.MARKER_TILTED_CROSS, 45, 1)

        #Show result
        cv2.imshow('res',frame)



def main():
    readCam = cv2.VideoCapture(0)


    while 1:
        trackWithDraw(readCam)
        k = cv2.waitKey(5) & 0xFF
        if k == 27:
            break

if __name__ == '__main__':
    main()
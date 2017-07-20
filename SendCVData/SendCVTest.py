import socket
import sys
import cv2
import numpy as np

HOST = sys.argv[1]
PORT = 6969

client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

def sendData(MSG):
    client_socket.send(MSG + "\n")

def connectClient(THEHOST, THEPORT):
    client_socket.connect((THEHOST, THEPORT))

def trackWithNet(cap):

        #Take each frame
        _, frame = cap.read()


        #Convert BGR to HSV
        hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)

        #define range of green color in HSV
        lower_green = np.array([55,40,150])
        upper_green = np.array([85,255,255])

        #Threshold the HSV image to get only blue colors
        mask = cv2.inRange(hsv, lower_green, upper_green)
        im2, contours, hierarchy = cv2.findContours(mask,cv2.RETR_EXTERNAL,cv2.CHAIN_APPROX_SIMPLE)

        cx=0
        cy=0

        #Make sure there are contours
        if contours:

            #Find largest contour
            c = max(contours, key = cv2.contourArea)

            #Calculate centroid using moment data
            moment = cv2.moments(c)

            #Prevent divide by zero
            if moment['m00'] > 0:
                cx = int(moment['m10']/moment['m00'])
                cy = int(moment['m01']/moment['m00'])

        return(`cx`+":"+`cy`)

def main():
    readCam = cv2.VideoCapture(0)

    connectClient(HOST, PORT)

    while 1:
        sendData(trackWithNet(readCam))

if __name__ == '__main__':
    main()
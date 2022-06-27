import { useRef, useEffect, useState } from 'react';
import SimplePeer, { Instance, SignalData } from "simple-peer";
// import { useSelector, useDispatch } from 'react-redux';
const webSocketConnection = new WebSocket("ws://192.168.0.17:8080/videochat");

const VideoCall = () => {
    // let errorMsg = "some error";
    const [errorMsg, setErrorMsg] = useState("");
    const videoSelf = useRef(null);
    const videoCaller = useRef(null);
    const [connectionStatus, setConnectionStatus] = useState(null);
    const [offerSignal, setOfferSignal] = useState();
    const ConnectionStatus = {
      OFFERING: 'OFFERING',
      RECEIVING: 'RECEIVING',
      CONNECTED: 'CONNECTED',
    }

    const [simplePeer, setSimplePeer] = useState();

    const sendOrAcceptInvitation = (isInitiator, offer) => {
      navigator.mediaDevices
        .getUserMedia({ video: true, audio: false })
        .then((mediaStream) => {
          const video = videoSelf.current;
          // video!.srcObject = mediaStream;
          // video!.play();
          // video.srcObject = mediaStream; // eslint-disable-line
          // video.play(); // eslint-disable-line
          if (video) {
            video.srcObject = mediaStream;
            video.play();
          }

          const sp = new SimplePeer({
            trickle: false,
            initiator: isInitiator,
            stream: mediaStream,
          });

          if (isInitiator) setConnectionStatus(ConnectionStatus.OFFERING);
          else offer && sp.signal(offer);

          sp.on("signal", (data) => webSocketConnection.send(JSON.stringify(data)));
          sp.on("connect", () => setConnectionStatus(ConnectionStatus.CONNECTED));
          sp.on("stream", (stream) => {
            const video = videoCaller.current;
            // video!.srcObject = stream;
            // video!.play();
            // video?.srcObject = mediaStream;
            // video?.play();
            if (video) {
              video.srcObject = mediaStream;
              video.play();
            }
          });
          setSimplePeer(sp);
        }).catch((err) => {
          // errorMsg = err;
          setErrorMsg(err.toString());
          console.log(errorMsg);
          console.log(err);
        });
    };

    useEffect(() => {
      webSocketConnection.onmessage = (message) => {
        const payload = JSON.parse(message.data);
        if (payload?.type === "offer") {
          setOfferSignal(payload);
          setConnectionStatus(ConnectionStatus.RECEIVING);
        } else if (payload?.type === "answer") simplePeer?.signal(payload);
      };
    }, [simplePeer]);

    return (
      <div className="web-rtc-page">
        {connectionStatus === null && (
          <button style={{marginLeft: '50px', marginTop: '50px'}} onClick={() => sendOrAcceptInvitation(true)}>CALL</button>
        )}
        {connectionStatus === ConnectionStatus.OFFERING && (
          <div className="loader"></div>
        )}
        {connectionStatus === ConnectionStatus.RECEIVING && (
          <button onClick={() => sendOrAcceptInvitation(false, offerSignal)}>
            ANSWER CALL
          </button>
        )}
      <div className="video-container">
        <video ref={videoSelf} className="video-block" />
        <video ref={videoCaller} className="video-block" />
      </div>
      <h1>{errorMsg}</h1>
    </div>
  );
};

export default VideoCall;

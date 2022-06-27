import React, { useState, useEffect } from 'react';

import OTPpopup from './OTPpopup';
import EnterMail from './EnterMail';
import NewPassword from './NewPassword';

const Forgotpassword = () => {
  const [email, setEmail] = useState('');

  const [stage, setStage] = useState(1);

  useEffect(() => {
    if (stage) setStage(stage)
  }, [stage])


  return (
    <div>
      {stage === 1 && (
        <div>
          <EnterMail email={email} setEmail={setEmail} setStage={setStage} />
        </div>
      )}

      {stage === 2 && (
        <div>
          <OTPpopup setStage={setStage} />
        </div>
      )}

      {stage === 3 && (
        <div>
          <NewPassword email={email} />
        </div>
      )}

    </div>
  );
}

export default Forgotpassword;

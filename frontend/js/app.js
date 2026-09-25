async function login(){
  const id = document.getElementById('id').value.trim();
  const pw = document.getElementById('pw').value;
  const err = document.getElementById('error');
  err.textContent = '';
  try{
    const res = await fetch('http://localhost:8080/api/login',{
      method:'POST',
      headers:{'Content-Type':'application/json'},
      body:JSON.stringify({id,pw})
    });
    if(res.ok){
      try{ localStorage.setItem('userId', id); }catch(e){}
      window.location.href = 'home.html';
    } else {
      err.textContent = 'Invalid login';
    }
  }catch(e){
    err.textContent = 'Network error';
  }
}

async function register(){
  const email = document.getElementById('reg-email').value.trim();
  const id = document.getElementById('reg-id').value.trim();
  const pw = document.getElementById('reg-pw').value;
  const confirm = document.getElementById('reg-confirm').value;
  const msg = document.getElementById('reg-msg');
  msg.textContent='';
  if(!/^[^@]+@sus\.edu$/i.test(email)){ msg.textContent='Use a sus.edu email'; return; }
  if(!/[A-Z]/.test(pw) || !/[0-9]/.test(pw)){ msg.textContent='Password needs a capital letter and a number'; return; }
  if(pw!==confirm){ msg.textContent='Passwords do not match'; return; }
  try{
    const res = await fetch('http://localhost:8080/api/register',{
      method:'POST',
      headers:{'Content-Type':'application/json'},
      body:JSON.stringify({id,email,pw,confirm})
    });
    if(res.ok){
      msg.textContent='Account created';
      msg.className='success';
    } else {
      msg.textContent='Account creation failed';
      msg.className='error';
    }
  }catch(e){
    msg.textContent='Network error';
    msg.className='error';
  }
}

async function loadSchedule(){
  const uid = localStorage.getItem('userId');
  const el = document.getElementById('sched-list');
  if(!uid){ el.textContent = 'Please login first'; return; }
  el.textContent='';
  try{
    const res = await fetch('http://localhost:8080/api/schedule?userId='+encodeURIComponent(uid));
    if(!res.ok){ el.textContent='Failed to load'; return; }
    const data = await res.json();
    const ul = document.createElement('ul');
    (data.classes||[]).forEach(c=>{ const li=document.createElement('li'); li.textContent=c; ul.appendChild(li); });
    el.appendChild(ul);
  }catch(e){ el.textContent='Network error'; }
}

async function saveSchedule(){
  const uid = localStorage.getItem('userId');
  const input = document.getElementById('sched-input');
  const msg = document.getElementById('sched-msg');
  msg.textContent='';
  if(!uid){ msg.textContent='Please login first'; return; }
  const raw = input.value.trim();
  const codes = raw.split(',').map(s=>s.trim().toUpperCase()).filter(Boolean);
  if(codes.length===0){ msg.textContent='Enter at least one class code'; return; }
  for(const c of codes){ if(!/^[A-Z]{3}[0-9]{3}$/.test(c)){ msg.textContent='Use codes like ENG205, MAT101, PHY150'; return; } }
  const classes = codes.join(',');
  try{
    const res = await fetch('http://localhost:8080/api/schedule',{
      method:'POST', headers:{'Content-Type':'application/json'},
      body:JSON.stringify({userId:uid, classes:classes, action:'set'})
    });
    if(res.ok){ msg.textContent='Saved'; loadSchedule(); }
    else { msg.textContent='Save failed'; }
  }catch(e){ msg.textContent='Network error'; }
}


async function loadAlerts(){
  const el = document.getElementById('alerts');
  el.innerHTML = '';
  try{
    const res = await fetch('http://localhost:8080/api/alerts');
    if(!res.ok){ el.textContent = 'Failed to load alerts'; return; }
    const list = await res.json();
    if(!Array.isArray(list) || list.length===0){ el.textContent = 'No alerts'; return; }
    const ul = document.createElement('ul');
    for(const a of list){
      const li = document.createElement('li');
      li.textContent = `${a.class}: ${a.message}`;
      ul.appendChild(li);
    }
    el.appendChild(ul);
  }catch(e){
    el.textContent = 'Network error';
  }
}

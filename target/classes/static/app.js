const API='/api';
async function request(url,options={}){const r=await fetch(API+url,{headers:{'Content-Type':'application/json'},...options});if(!r.ok){let e={};try{e=await r.json()}catch{}throw new Error(e.error||'Request failed');}if(r.status===204)return null;return r.json();}
const money=n=>new Intl.NumberFormat('en-IN',{style:'currency',currency:'INR'}).format(n||0);
const date=s=>s||'—';
function status(s){return `<span class="status ${s}">${s}</span>`}
async function dashboard(){try{const d=await request('/invoices/dashboard');Object.entries(d).forEach(([k,v])=>{const el=document.getElementById(k);if(el)el.textContent=k==='revenue'?money(v):v;});const list=await request('/invoices');document.getElementById('recentRows').innerHTML=list.slice(-6).reverse().map(i=>`<tr><td>${i.invoiceNumber}</td><td>${i.customer?.name||''}</td><td>${date(i.issueDate)}</td><td>${date(i.dueDate)}</td><td>${money(i.total)}</td><td>${status(i.status)}</td></tr>`).join('')||'<tr><td colspan="6">No invoices yet.</td></tr>';}catch(e){console.error(e)}}
let invoiceCache=[];
async function loadInvoices(){invoiceCache=await request('/invoices');renderInvoices();}
function renderInvoices(){const q=(document.getElementById('search')?.value||'').toLowerCase(),f=document.getElementById('filter')?.value||'';const rows=invoiceCache.filter(i=>(!f||i.status===f)&&(`${i.invoiceNumber} ${i.customer?.name||''}`).toLowerCase().includes(q));document.getElementById('invoiceRows').innerHTML=rows.map(i=>`<tr><td>${i.invoiceNumber}</td><td>${i.customer?.name||''}</td><td>${date(i.issueDate)}</td><td>${date(i.dueDate)}</td><td>${money(i.total)}</td><td>${status(i.status)}</td><td><select onchange="changeStatus(${i.id},this.value)"><option value="">Change status</option><option>PAID</option><option>PENDING</option><option>OVERDUE</option></select> <button onclick="removeInvoice(${i.id})">Delete</button></td></tr>`).join('')||'<tr><td colspan="7">No matching invoices.</td></tr>';}
async function changeStatus(id,statusValue){if(!statusValue)return;try{await request(`/invoices/${id}/status`,{method:'PATCH',body:JSON.stringify({status:statusValue})});await loadInvoices();}catch(e){alert(e.message)}}
async function removeInvoice(id){if(!confirm('Delete this invoice?'))return;try{await request(`/invoices/${id}`,{method:'DELETE'});await loadInvoices()}catch(e){alert(e.message)}}
async function loadCustomers(){
    const customers=await request('/customers');
    const select=document.getElementById('customerSelect');
    if(select)
        select.innerHTML='<option value="">Choose customer</option>'+
            customers.map(c=>`<option value="${c.id}">${c.name} (${c.email})</option>`).join('');
    const rows=document.getElementById('customerRows');
    if(rows)
        rows.innerHTML=customers.map(c=>`<tr><td>${c.name}</td><td>${c.email}</td><td>${c.phone||'—'}</td><td>${c.address||'—'}</td></tr>`).join('')||'<tr><td colspan="4">No customers yet.</td></tr>';
}
document.getElementById('customerForm')?.addEventListener('submit',async e=>{e.preventDefault();const data=Object.fromEntries(new FormData(e.target));try{await request('/customers',{method:'POST',body:JSON.stringify(data)});e.target.reset();loadCustomers();}catch(err){alert(err.message)}});
function addItem(){
    const div=document.createElement('div');
    div.className='item-row';
    div.innerHTML='<input placeholder="Description" class="desc" required>' +
                  '<input type="number" placeholder="Qty" class="qty" min="1" value="1" required>' +
                 '<input type="number" placeholder="Unit price" class="price" min="0" step=".01" required>';
    document.getElementById('items').appendChild(div);
}
document.getElementById('invoiceForm')?.addEventListener('submit',async e=>{
    e.preventDefault();
    const f=new FormData(e.target);
    const items=[...document.querySelectorAll('.item-row')].map(row=>({description:row.querySelector('.desc').value,
                                                                                        quantity:row.querySelector('.qty').value,
                                                                                        unitPrice:row.querySelector('.price').value}));
    const body={customerId:f.get('customerId'),
                     issueDate:f.get('issueDate'),
                     dueDate:f.get('dueDate'),
                     taxRate:f.get('taxRate'),
                    items};
       try{
           await request('/invoices',{method:'POST',body:JSON.stringify(body)});
           alert('Invoice created successfully');location.href='/invoices.html';
       }
       catch(err){
           alert(err.message)
       }
}
);
if(location.pathname==='/'||location.pathname==='/index.html')dashboard();
if(location.pathname==='/invoices.html')loadInvoices().catch(e=>alert(e.message));
if(location.pathname==='/customers.html'||location.pathname==='/create-invoice.html')loadCustomers().catch(e=>alert(e.message));

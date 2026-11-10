// Front-end simples que consome a API REST (mesma origem, sem CORS).
const API = '/api';
const $ = (s, r = document) => r.querySelector(s);
const esc = v => String(v ?? '').replace(/[&<>"']/g, c => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));
const money = v => Number(v ?? 0).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
const tag = (ok, a, b) => `<span class="tag ${ok ? 'ok' : 'ruim'}">${ok ? a : b}</span>`;

function aviso(msg, tipo) {
  const el = $('#aviso');
  el.textContent = msg; el.className = tipo; el.style.display = 'block';
  clearTimeout(aviso.t); aviso.t = setTimeout(() => el.style.display = 'none', 5000);
}

// Chamada à API: mostra a mensagem de erro devolvida pela API (400, 404, 409...)
async function api(path, opts = {}) {
  const r = await fetch(API + path, { headers: { 'Content-Type': 'application/json' }, ...opts });
  if (r.status === 204) return null;
  const dados = await r.json().catch(() => ({}));
  if (!r.ok) throw new Error(`${r.status} - ${dados.erro || 'Erro na requisição'}`);
  return dados;
}
const tentar = async fn => { try { await fn(); } catch (e) { aviso(e.message, 'erro'); } };

// Descrição das telas de cadastro simples
const TELAS = {
  livros: { titulo: 'Livro', path: '/livros', campos: [
    { k: 'titulo', l: 'Título', req: 1 }, { k: 'isbn', l: 'ISBN' },
    { k: 'preco', l: 'Preço', t: 'number', step: '0.01', req: 1 },
    { k: 'quantidadeEstoque', l: 'Estoque', t: 'number', req: 1 },
    { k: 'estoqueMinimo', l: 'Estoque mínimo', t: 'number' },
    { k: 'categoriaId', l: 'Categoria', src: '/categorias', req: 1 },
    { k: 'editoraId', l: 'Editora', src: '/editoras', req: 1 }],
    cols: [['titulo', 'Título'], ['nomeCategoria', 'Categoria'], ['preco', 'Preço', money], ['quantidadeEstoque', 'Estoque', (v, l) => v <= l.estoqueMinimo ? `<span class="tag ruim">${v} (baixo)</span>` : v]] },
  clientes: { titulo: 'Cliente', path: '/clientes', campos: [
    { k: 'nome', l: 'Nome', req: 1 }, { k: 'cpf', l: 'CPF', req: 1 }, { k: 'email', l: 'E-mail', t: 'email' },
    { k: 'telefone', l: 'Telefone' }, { k: 'estado', l: 'UF', max: 2 }],
    cols: [['nome', 'Nome'], ['cpf', 'CPF'], ['email', 'E-mail'], ['ativo', 'Situação', v => tag(v, 'Ativo', 'Inativo')]] },
  categorias: { titulo: 'Categoria', path: '/categorias', campos: [
    { k: 'nome', l: 'Nome', req: 1 }, { k: 'descricao', l: 'Descrição' }],
    cols: [['nome', 'Nome'], ['descricao', 'Descrição']] },
  editoras: { titulo: 'Editora', path: '/editoras', campos: [
    { k: 'nome', l: 'Nome', req: 1 }, { k: 'cnpj', l: 'CNPJ' }, { k: 'email', l: 'E-mail', t: 'email' }, { k: 'telefone', l: 'Telefone' }],
    cols: [['nome', 'Nome'], ['cnpj', 'CNPJ'], ['email', 'E-mail']] }
};

async function telaCadastro(nome) {
  const t = TELAS[nome];
  const selects = {};
  for (const c of t.campos.filter(c => c.src)) selects[c.k] = await api(c.src);
  const campo = c => c.src
    ? `<label>${c.l}<select name="${c.k}" required><option value="">Selecione</option>${selects[c.k].map(o => `<option value="${o.id}">${esc(o.nome)}</option>`).join('')}</select></label>`
    : `<label>${c.l}<input name="${c.k}" type="${c.t || 'text'}" ${c.step ? `step="${c.step}"` : ''} ${c.max ? `maxlength="${c.max}"` : ''} ${c.req ? 'required' : ''}></label>`;
  const filtros = nome === 'livros' ? `
    <section><h2>Buscar livros</h2><div class="filtros">
      <label>Título<input id="f-titulo" placeholder="Ex.: Dom Casmurro"></label>
      <label>Preço mínimo<input id="f-min" type="number" step="0.01"></label>
      <label>Preço máximo<input id="f-max" type="number" step="0.01"></label>
      <button class="btn" id="f-ok">Buscar</button><button class="btn sec" id="f-limpar">Limpar</button>
    </div></section>` : '';
  $('#app').innerHTML = `
    <section><h2>Novo ${t.titulo.toLowerCase()}</h2><form id="form">${t.campos.map(campo).join('')}<button class="btn">Cadastrar ${t.titulo.toLowerCase()}</button></form></section>
    ${filtros}<section><h2>${t.titulo}s cadastrados</h2><div class="tabela" id="lista"></div></section>`;

  const carregar = async () => {
    let url = t.path;
    if (nome === 'livros') {
      const ti = $('#f-titulo').value.trim(), mn = $('#f-min').value, mx = $('#f-max').value;
      if (ti) url += '/buscar?titulo=' + encodeURIComponent(ti);
      else if (mn || mx) url += `/filtrar?${mn ? 'precoMin=' + mn : ''}&${mx ? 'precoMax=' + mx : ''}`;
    }
    const itens = await api(url);
    $('#lista').innerHTML = itens.length ? `<table><thead><tr>${t.cols.map(c => `<th>${c[1]}</th>`).join('')}<th></th></tr></thead><tbody>${
      itens.map(i => `<tr>${t.cols.map(c => `<td>${c[2] ? c[2](i[c[0]], i) : esc(i[c[0]])}</td>`).join('')}<td><button class="btn mini perigo" data-del="${i.id}">Excluir</button></td></tr>`).join('')}</tbody></table>`
      : '<p class="vazio">Nada cadastrado ainda. Use o formulário acima para começar.</p>';
  };

  $('#form').onsubmit = e => { e.preventDefault(); tentar(async () => {
    const corpo = { ativo: true };
    for (const c of t.campos) {
      const v = e.target[c.k].value.trim();
      if (v !== '') corpo[c.k] = (c.t === 'number' || c.src) ? Number(v) : v;
    }
    await api(t.path, { method: 'POST', body: JSON.stringify(corpo) });
    e.target.reset(); aviso(`${t.titulo} cadastrado com sucesso.`, 'ok'); await carregar();
  }); };
  $('#lista').onclick = e => { const id = e.target.dataset.del; if (id && confirm('Excluir este registro?')) tentar(async () => {
    await api(`${t.path}/${id}`, { method: 'DELETE' }); aviso('Registro excluído.', 'ok'); await carregar(); }); };
  if (nome === 'livros') {
    $('#f-ok').onclick = () => tentar(carregar);
    $('#f-limpar').onclick = () => { ['#f-titulo', '#f-min', '#f-max'].forEach(s => $(s).value = ''); tentar(carregar); };
  }
  await carregar();
}

async function telaVendas() {
  const [clientes, livros] = await Promise.all([api('/clientes'), api('/livros')]);
  const opcLivro = livros.map(l => `<option value="${l.id}">${esc(l.titulo)} (${l.quantidadeEstoque} em estoque)</option>`).join('');
  $('#app').innerHTML = `
    <section><h2>Registrar venda</h2><form id="form">
      <label>Cliente<select name="clienteId" required><option value="">Selecione</option>${clientes.map(c => `<option value="${c.id}">${esc(c.nome)}</option>`).join('')}</select></label>
      <label>Forma de pagamento<select name="formaPagamento"><option>PIX</option><option>CARTAO</option><option>DINHEIRO</option></select></label>
      <label>Desconto (R$)<input name="desconto" type="number" step="0.01" min="0" value="0"></label>
      <div class="itens" id="itens"></div>
      <button type="button" class="btn sec" id="add">Adicionar livro</button>
      <button class="btn">Registrar venda</button>
    </form></section>
    <section><h2>Vendas registradas</h2><div class="tabela" id="lista"></div></section>`;
  const addItem = () => $('#itens').insertAdjacentHTML('beforeend', `<div class="filtros"><label>Livro<select class="i-livro" required><option value="">Selecione</option>${opcLivro}</select></label><label>Quantidade<input class="i-qtd" type="number" min="1" value="1" required></label></div>`);
  addItem(); $('#add').onclick = addItem;

  const carregar = async () => {
    const vendas = await api('/vendas');
    $('#lista').innerHTML = vendas.length ? `<table><thead><tr><th>Nº</th><th>Cliente</th><th>Itens</th><th>Total</th><th>Situação</th><th></th></tr></thead><tbody>${
      vendas.map(v => `<tr><td>${v.id}</td><td>${esc(v.nomeCliente)}<br><small>${new Date(v.dataVenda).toLocaleString('pt-BR')}</small></td>
        <td>${v.itens.map(i => `${i.quantidade}x ${esc(i.tituloLivro)}`).join('<br>')}</td><td>${money(v.valorTotal)}</td>
        <td>${tag(v.status === 'CONCLUIDA', 'Concluída', 'Cancelada')}</td>
        <td>${v.status === 'CONCLUIDA' ? `<button class="btn mini perigo" data-cancel="${v.id}">Cancelar</button>` : `<button class="btn mini perigo" data-del="${v.id}">Excluir</button>`}</td></tr>`).join('')}</tbody></table>`
      : '<p class="vazio">Nenhuma venda registrada. Cadastre um cliente e um livro e registre a primeira venda.</p>';
  };

  $('#form').onsubmit = e => { e.preventDefault(); tentar(async () => {
    const itens = [...document.querySelectorAll('#itens .filtros')].map(d => ({ livroId: Number($('.i-livro', d).value), quantidade: Number($('.i-qtd', d).value) }));
    await api('/vendas', { method: 'POST', body: JSON.stringify({ clienteId: Number(e.target.clienteId.value), formaPagamento: e.target.formaPagamento.value, desconto: Number(e.target.desconto.value || 0), itens }) });
    aviso('Venda registrada. O estoque foi atualizado.', 'ok'); await telaVendas();
  }); };
  $('#lista').onclick = e => {
    const { cancel, del } = e.target.dataset;
    if (cancel) tentar(async () => { await api(`/vendas/${cancel}/cancelar`, { method: 'PUT' }); aviso('Venda cancelada. Os livros voltaram ao estoque.', 'ok'); await telaVendas(); });
    if (del && confirm('Excluir esta venda?')) tentar(async () => { await api(`/vendas/${del}`, { method: 'DELETE' }); aviso('Venda excluída.', 'ok'); await carregar(); });
  };
  await carregar();
}

function abrir(nome) {
  document.querySelectorAll('#tabs button').forEach(b => b.classList.toggle('on', b.dataset.tab === nome));
  tentar(() => nome === 'vendas' ? telaVendas() : telaCadastro(nome));
}
$('#tabs').onclick = e => { if (e.target.dataset.tab) abrir(e.target.dataset.tab); };
abrir('livros');

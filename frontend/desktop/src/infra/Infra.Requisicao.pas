unit Infra.Requisicao;

interface

uses Infra.Requisicao.Intf, RESTRequest4D, System.JSON;

type
  TMetodoHttp = (MetodoGet, MetodoPost, MetodoPut, MetodoDelete);
  TRequisicao = class(TInterfacedObject, IRequisicao)
  private
    FRequisicao: RESTRequest4D.IRequest;
    constructor Create;
    function Executar(const AMetodo: TMetodoHttp): IResponse;
    function Recurso(const ARecurso: string): IRequisicao;
    function Get: IResponse;
    function Post: IResponse;
    function Put: IResponse;
    function Delete: IResponse;
    function AdicionarCorpo(const ACorpo: TJSONObject): IRequisicao;
    function RenovarTokens: Boolean;
    function Enviar(const AMetodo: TMetodoHttp): IResponse;
  public
    class function Nova: IRequisicao;
  end;

implementation

uses Infra.Configuracao, Infra.Sessao, Infra.Erros;

{ TRequisicao }

function TRequisicao.AdicionarCorpo(const ACorpo: TJSONObject): IRequisicao;
begin

 FRequisicao.AddBody(Acorpo);
 Result := Self;

end;

constructor TRequisicao.Create;
begin

  inherited Create;
  FRequisicao := RESTRequest4D.TRequest.New.BaseURL(TConfiguracao.UrlApi);

end;

function TRequisicao.Delete: IResponse;
begin

  Result := Executar(MetodoDelete);

end;

function TRequisicao.Enviar(const AMetodo: TMetodoHttp): IResponse;
begin

  if TSessao.Ativa then
    FRequisicao.TokenBearer(TSessao.AccessToken);

  case AMetodo of
    MetodoGet:    Result := FRequisicao.Get;
    MetodoPost:   Result := FRequisicao.Post;
    MetodoPut:    Result := FRequisicao.Put;
    MetodoDelete: Result := FRequisicao.Delete;
  end;

end;

function TRequisicao.Executar(const AMetodo: TMetodoHttp): IResponse;
begin

  Result := Enviar(AMetodo);

  if (Result.StatusCode = 401) and TSessao.Ativa then
  begin

    if RenovarTokens then
      Result := Enviar(AMetodo)
    else
      TSessao.Encerrar;

  end;

  TErros.Validar(Result);

end;

function TRequisicao.Get: IResponse;
begin

  Result := Executar(MetodoGet);

end;

class function TRequisicao.Nova: IRequisicao;
begin

  Result := TRequisicao.Create;

end;

function TRequisicao.Post: IResponse;
begin

  Result := Executar(MetodoPost);

end;

function TRequisicao.Put: IResponse;
begin

  Result := Executar(MetodoPut);

end;

function TRequisicao.Recurso(const ARecurso: string): IRequisicao;
begin

  FRequisicao.Resource(ARecurso);
  Result := Self;

end;

function TRequisicao.RenovarTokens: Boolean;
var
  LCorpo : TJSONObject;
  LResposta : IResponse;
  LTokens : TJSONObject;
begin

  LCorpo := TJSONObject.Create;
  LCorpo.AddPair('refreshToken', TSessao.RefreshToken);

  LResposta := RESTRequest4D.TRequest.New
  .BaseURL(TConfiguracao.UrlApi)
  .Resource('/v1/autenticacao/refresh')
  .AddBody(LCorpo)
  .Put;

  Result := LResposta.StatusCode = 200;

  if Result then
  begin

    LTokens := LResposta.JSONValue AS TJSONObject;
    TSessao.AtualizarTokens(LTokens.GetValue<string>('accessToken'),LTokens.GetValue<string>('refreshToken'));

  end;

end;

end.

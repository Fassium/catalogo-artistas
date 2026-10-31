unit Infra.Requisicao;

interface

uses Infra.Requisicao.Intf, RESTRequest4D, System.JSON;

type
  TRequisicao = class(TInterfacedObject, IRequisicao)
  private
    FRequisicao: RESTRequest4D.IRequest;
    constructor Create;
    function Recurso(const ARecurso: string): IRequisicao;
    function Get: IResponse;
    function Post: IResponse;
    function Put: IResponse;
    function AdicionarCorpo(const ACorpo: TJSONObject): IRequisicao;
  public
    class function Nova: IRequisicao;
  end;

implementation

uses Infra.Configuracao, Infra.Sessao;

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

function TRequisicao.Get: IResponse;
begin

  FRequisicao.TokenBearer(TSessao.AccessToken);
  Result := FRequisicao.Get;

end;

class function TRequisicao.Nova: IRequisicao;
begin

  Result := TRequisicao.Create;

end;

function TRequisicao.Post: IResponse;
begin

  FRequisicao.TokenBearer(TSessao.AccessToken);
  Result := FRequisicao.Post;

end;

function TRequisicao.Put: IResponse;
begin

  FRequisicao.TokenBearer(TSessao.AccessToken);
  Result := FRequisicao.Put;

end;

function TRequisicao.Recurso(const ARecurso: string): IRequisicao;
begin

  FRequisicao.Resource(ARecurso);
  Result := Self;

end;

end.

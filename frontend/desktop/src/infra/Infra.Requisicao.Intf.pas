unit Infra.Requisicao.Intf;

interface

uses
  RESTRequest4D, System.JSON;

type
  IResponse = RESTRequest4D.IResponse;

  IRequisicao = interface
    ['{657D1405-CCDD-4359-A2FA-0D5E31EABF89}']
    function Recurso(const ARecurso: string): IRequisicao;
    function Get: IResponse;
    function Post: IResponse;
    function Put: IResponse;
    function AdicionarCorpo(const ACorpo: TJSONObject): IRequisicao;
  end;

implementation

end.

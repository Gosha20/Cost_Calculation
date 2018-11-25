package parser

import DataBase.DBRes

trait Command {
  def execute():DBRes[Unit]
}
